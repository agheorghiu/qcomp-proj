package representation;

import java.util.*;

/**
 * 
 * Implementation of quantum register as an array
 * 
 * @author Andru, Charlie, Sam
 * 
 */

public class ARegister extends IRegister {

	/**
	 * 
	 * Number of qubits to be used
	 * 
	 */
	private final int numQubits;

	/**
	 * 
	 * Register implemented as an array
	 * 
	 */
	private Complex[] reg;

	/**
	 * 
	 * Default constructor. Initialises to state 0 (|000...0>) with amplitude 1
	 * 
	 * @param numQubits
	 *            Number of qubits in the register
	 */
	public ARegister(int numQ) {
		numQubits = numQ;
		int nStates = 1 << numQubits;
		reg = new Complex[nStates];
		reg[0] = Complex.one();
		for (int i = 1; i < reg.length; i++)
			reg[i] = Complex.zero();
	}

	/**
	 * 
	 * Constuctor for array-based register
	 * 
	 * @param numQubits	number of qubits
	 * @param state	state on which it's operating
	 */
	public ARegister(int numQ, int state) {
		numQubits = numQ;
		reg = new Complex[numQubits];
		reg[state] = Complex.one();
		for (int i = 0; i < reg.length; i++)
			if (!(i == state))
				reg[i] = Complex.zero();
	}

	@Override
	public IRegister clone() {
		ARegister copy = new ARegister(this.numQubits);
		for (int i = 0; i < reg.length; i++)
			copy.setState(i, reg[i]);
		return copy;
	}

	@Override
	public void setState(int state, Complex amplitude) {
		reg[state] = amplitude.clone();
	}

	@Override
	public Complex getAmplitude(int state) {
		return reg[state];
	}

	@Override
	public Set<Integer> getStates() {
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < numQubits; i++)
			if (!reg[i].equals(Complex.zero()))
				set.add(new Integer(i));
		return set;
	}

	/**
	 * 
	 * Swap bits in binary representation of a number
	 * 
	 * @param state
	 *            state for which we swap bits
	 * @param index1
	 *            first index to be swapped
	 * @param index2
	 *            second index to be swapped
	 * @return returns integer with bits swapped
	 */
	private int swapBits(int state, int index1, int index2) {
		boolean bit1 = (((1 << index1) & state) > 0);
		boolean bit2 = (((1 << index2) & state) > 0);
		int newState = state;
		if (bit1)
			newState = newState | (1 << index2);
		else if (bit2)
			newState = newState ^ (1 << index2);

		if (bit2)
			newState = newState | (1 << index1);
		else if (bit1)
			newState = newState ^ (1 << index1);
		return newState;
	}

	@Override
	public void swapQubits(int index1, int index2) {
		Complex clone[] = reg.clone();
		for (int state = 0; state < reg.length; state++) {
			int swapped = swapBits(state, index1, index2);
			clone[swapped] = getAmplitude(state);
		}
		reg = clone;
	}

	@Override
	public List<Integer> getZeroStates(int index) {
		List<Integer> list = new ArrayList<Integer>();
		for (Integer state : getStates()) {
			if ((state & (1 << index)) == 0)
				list.add(state);
		}
		return list;
	}

	@Override
	public List<Integer> getOneStates(int index) {
		List<Integer> list = new ArrayList<Integer>();
		for (Integer state : getStates()) {
			if ((state & (1 << index)) != 0)
				list.add(state);
		}
		return list;
	}

	@Override
	public void nullifyStates(List<Integer> states) {
		for (Integer state : states)
			reg[state] = Complex.zero();
		normalise();
	}

	@Override
	public void normalise() {
		double normaliser = 0.0;
		for (int state = 0; state < reg.length; state++)
			normaliser += reg[state].modS();
		normaliser = Math.sqrt(normaliser);
		if (normaliser == 0)
			return;
		Complex divider = new Complex(1.0 / normaliser, 0);
		for (int state = 0; state < reg.length; state++)
			reg[state] = Complex.multiply(reg[state], divider);
	}

	/**
	 * 
	 * Returns binary representation of a given state
	 * 
	 * @param state
	 *            state for which we want to output binary representation
	 * @return returns binary representation of state
	 */
	private String toBinary(int state) {
		String str = new StringBuilder(Integer.toBinaryString(state)).reverse().toString();
		int len = str.length();
		for (int i = len; i <= numQubits; i++)
			str += "0";
		return str;
	}

	/**
	 * 
	 * Returns string representation of the register
	 * 
	 */
	public String toString() {
		String str = "";
		for (int state = 0; state < reg.length; state++)
			if (!reg[state].equals(Complex.zero()))
				str += toBinary(state) + ": " + getAmplitude(state) + "\n";
		return str;
	}

	public void print() {
		System.out.println(this.toString() + "\n");
	}
}