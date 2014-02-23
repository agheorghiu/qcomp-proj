package algorithms;

import gates.Hadamard;

import java.util.Set;

import operators.Operator;
import operators.OperatorFactory;
import operators.Projector;
import representation.Complex;
import representation.IRegister;
import representation.QRegister;

public class Grover implements Algorithm {
	
	/**
	 * 
	 * Register we are using
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
	 * Square root of N (where N = 2^numQubits)
	 * 
	 */
	private int rootN;
	
	/**
	 * 
	 * The needle in a haystack. The number/state we are looking for
	 * 
	 */
	private int omega;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param numQubits	number of qubits
	 * @param omega	value we are searching for
	 */
	public Grover(int numQubits, int omega) {
		this.reg = new QRegister(1);
		this.numQubits = numQubits;
		this.rootN = 1 << ((numQubits - 1) / 2 + (numQubits - 1) % 2);
		this.omega = omega;
	}

	/**
	 * 
	 * Runs the algorithm
	 * 
	 */
	@Override
	public void run() {
		OperatorFactory factory = new OperatorFactory(reg);
		Hadamard h = (Hadamard)factory.makeOperator("Hadamard");
		Projector p = new Projector(reg);
		Operator oracle = blackBox();
		Operator diffusion = diffusion();
		
		// apply H to all qubits
		for (int i = 0; i <= numQubits; i++) {
			h.setIndex(i);
			h.apply();
		}
		
		System.out.println("Initial superposition");
		System.out.println(reg);
		System.out.println();
		
		for (int i = 0; i < rootN; i++) {
			// apply the oracle operator
			oracle.apply();

			System.out.println("Applied blackbox");
			System.out.println(reg);
			System.out.println();

			
			// apply H to qubits 1 - numQubits
			for (int j = 1; j <= numQubits; j++) {
				h.setIndex(j);
				h.apply();
			}
			
			diffusion.apply();
			
			// apply H to qubits 1 - numQubits
			for (int j = 1; j <= numQubits; j++) {
				h.setIndex(j);
				h.apply();
			}
			
			System.out.println("After diffusion");
			System.out.println(reg);
			System.out.println();
		}
		
		// measure qubits 1 - numQubits and reconstruct omega from binary
		int computedOmega = 0;
		for (int i = 1; i <= numQubits; i++) {
			p.setIndex(i);
			if (p.apply())
				computedOmega += (1 << (i - 1));
		}
		
		System.out.println("Computed omega is: " + computedOmega);
	}

	/**
	 * 
	 * Oracle function. Take state |x>|q> -> |x>|q (+) f(x)>, where f(x) is 1 if x is the state we're looking for
	 * and 0 otherwise. (+) denotes xor operation.
	 * 
	 * @return	black box operator that identifies state we are searching for
	 */
	private Operator blackBox() {
		Operator function = new Operator(reg) {
			@Override
			public void apply() {
				Set<Integer> states = reg.getStates();
				for (Integer state : states)
					if ((state >> 1) == omega) {
						Complex aux = reg.getAmplitude(state);
						reg.setState(state, reg.getAmplitude(state ^ 1));
						reg.setState(state ^ 1, aux);
						return;
					}				
			}
		};
		return function;
	}
	
	/**
	 * 
	 * Diffusion operator, leaves 0 state unchanged but negates all the others. Since this is applied
	 * to all but the first qubit, we are interested in the 0 state for qubits 1-numQubits, so the verification
	 * is done for (state >> 1).
	 * 
	 * @return	returns diffusion operator
	 */
	private Operator diffusion() {
		Operator function = new Operator(reg) {
			@Override
			public void apply() {
				Set<Integer> states = reg.getStates();
				for (Integer state : states)
					if ((state >> 1) != 0)
						reg.setState(state, reg.getAmplitude(state).negated());
			}
		};
		return function;
	}
}
