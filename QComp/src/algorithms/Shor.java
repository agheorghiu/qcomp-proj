package algorithms;

import gates.CRK;
import gates.Hadamard;

import java.math.BigInteger;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import operators.Operator;
import operators.OperatorFactory;
import operators.Projector;
import representation.Complex;
import representation.IRegister;
import representation.QRegister;

/**
 * 
 * Implementation of Shor's algorithm
 * 
 * @author Andru, Charlie, Sam
 * 
 */
public class Shor implements Algorithm {

	/**
	 * 
	 * Register on which we operate
	 * 
	 */
	private IRegister reg;

	/**
	 * 
	 * Number of qubits
	 * 
	 */
	private int numQubits;

	/**
	 * 
	 * Number we want to factor
	 * 
	 */
	private int n;
	
	/**
	 * 
	 * Precision we want in comparing doubles
	 * 
	 */
	private final double EPS = 0.000001;

	/**
	 * 
	 * Constructor
	 * 
	 * @param n number we want to factor
	 */
	public Shor(int n) {
		this.n = n;
		this.numQubits = 2 * ((int) (Math.log10(n) / Math.log10(2)) + 1);
		this.reg = new QRegister(1 << (numQubits / 2));
	}

	@Override
	public void run() {
		int pow2 = 0;
		// remove powers of 2
		while (n % 2 == 0) {
			pow2++;
			n = n / 2;
		}
		if (pow2 > 0)
			System.out.print("(2^" + pow2 + ") * ");
		// check if n is prime
		BigInteger N = new BigInteger(n + "");
		if (n == 1 || N.isProbablePrime(10)) { // confidence 10 means probability of error < 1/1024
			System.out.println(n);
			return;
		}
		// if n is not prime, factor it
		int fact = 1;
		while (fact == 1)
			fact = factor(n);
		System.out.println(fact + " " + n / fact);
	}

	/**
	 * 
	 * Compute a factor of n
	 * 
	 * @param n number which we want to factor
	 * @return factor of n
	 */
	private int factor(int n) {
		Random generator = new Random();
		BigInteger N = new BigInteger(n + "");
		while (true) {
			int rand = 0, period = 1;
			// find period of modular exponentiating function
			while (period == 1) {
				rand = generator.nextInt(n - 1) + 1;
				int gcd = N.gcd(new BigInteger(rand + "")).intValue();
				if (gcd != 1)
					return gcd;
				period = findPeriod(rand, n); 
			}
			// use period to find suitable candidate for factor
			BigInteger pow = new BigInteger(rand + "");
			BigInteger incPow = pow.add(new BigInteger("1"));
			BigInteger decPow = pow.subtract(new BigInteger("1"));
			pow = pow.pow(period / 2);
			// check candidates
			if (period % 2 == 0 && !incPow.mod(N).equals(BigInteger.ZERO)) {
				int candidate1 = incPow.gcd(N).intValue();
				int candidate2 = decPow.gcd(N).intValue();
				if (candidate1 != 1)
					return candidate1;
				if (candidate2 != 1)
					return candidate2;
			}
		}
	}

	/**
	 * 
	 * Method for finding the period of the function a^x mod n, where
	 * a is rand
	 * 
	 * @param rand	base of modular exponentiation
	 * @param n	number we want to factor
	 * @return	order of rand
	 */
	private int findPeriod(int rand, int n) {
		Operator blackBox = blackBox(rand, n, numQubits / 2);
		Operator fourier = fourier(numQubits / 2);
		Operator invFourier = invFourier(numQubits / 2);
		Projector proj = new Projector(reg);
		// create initial superposition
		fourier.apply();
		// apply black box operator to get all values of (rand^i mod n) in the superposition
		blackBox.apply();
		// apply inverse Fourier to undo the superposition
		invFourier.apply();
		// measure first qubits to recover the period
		double measured = 0.0;
		for (int i = 0; i < numQubits / 2; i++) {
			proj.setIndex(i);
			if (proj.apply())
				measured += Math.pow(2, -(i + 1));
		}
		// use continued fraction expansion to get period
		return getDenominator(measured);
	}
	
	/**
	 * 
	 * Black box operator which takes state |i> |1> -> |i> |base^i mod n> 
	 * 
	 * @param base	base that we are exponentiating
	 * @param n		number we are factoring
	 * @param numQubits	number of qubits
	 * @return	returns black box operator
	 */
	private Operator blackBox(final int base, final int n, final int numQubits) {
		return new Operator(reg) {
			@Override
			public void apply() {
				Set<Integer> states = reg.getStates();
				int mask = (1 << numQubits) - 1;
				for (Integer state : states) {
					int firstHalf = state & mask;
					int secondHalf = (int)Math.pow(base, firstHalf) % n;
					int otherState = firstHalf + (secondHalf << numQubits);
					Complex aux = reg.getAmplitude(state);
					reg.setState(state, reg.getAmplitude(otherState));
					reg.setState(otherState, aux);
				}
			}
		};
	}
	
	/**
	 * 
	 * Quantum Fourier Transform (QFT) operator applied to the first numQubits qubits
	 * 
	 * @return	returns QFT operator
	 */
	private Operator fourier(final int numQubits) {
		final OperatorFactory factory = new OperatorFactory(reg);
		return new Operator(reg) {
			@Override
			public void apply() {
				Hadamard h = (Hadamard)factory.makeOperator("Hadamard");
				CRK crk = (CRK)factory.makeOperator("CRK");
				for (int i = 0; i < numQubits; i++) {
					h.setIndex(i);
					h.apply();
					for (int j = i + 1; j < numQubits; j++) {
						crk.setPhaseParam(j - i + 1);
						crk.setIndices(j, i);
						crk.apply();
					}
				}
			}
		};		
	}
	
	/**
	 * 
	 * Inverse Quantum Fourier Transform (IQFT) operator applied to the first numQubits qubits
	 * 
	 * @return	returns IQFT operator
	 */
	private Operator invFourier(final int numQubits) {
		final OperatorFactory factory = new OperatorFactory(reg);
		return new Operator(reg) {
			@Override
			public void apply() {
				Hadamard h = (Hadamard)factory.makeOperator("Hadamard");
				CRK crk = (CRK)factory.makeOperator("CRK");
				for (int i = numQubits - 1; i >= 0; i--) {
					for (int j = numQubits - 1; j >= i + 1; j--) {
						crk.setPhaseParam(j + 1 - i);
						crk.setIndices(j, i);
						crk.apply();
					}
					h.setIndex(i);
					h.apply();
				}
			}
		};		
	}

	/**
	 * 
	 * Use continued fraction expansion to get the denominator of a fraction
	 * 
	 * @param frac	fraction
	 * @return	denominator
	 */
	private int getDenominator(double frac) {
		Stack<Integer> continuedFraction = new Stack<Integer>();
		double current = frac;

		// create continued fraction expansion
		continuedFraction.push((int)Math.floor(current));
		while (current - Math.floor(current) > EPS) {
			current = 1.0 / (current - Math.floor(current));
			continuedFraction.push((int)Math.floor(current));
		}

		// compute original irreducible fraction by determining numerator and denominator
		int numerator = 0, denominator = 1;
		while (!continuedFraction.isEmpty()) {
			int elem = continuedFraction.pop();
			int newNum = denominator; 
			int newDenom = elem * denominator + numerator;
			numerator = newNum;
			denominator = newDenom;
		}

		// swap values
		int aux = numerator;
		numerator = denominator;
		denominator = aux;

		return denominator;
	}

}
