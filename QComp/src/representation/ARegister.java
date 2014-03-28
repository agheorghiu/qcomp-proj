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
	 * Register implemented as an array
	 * 
	 */
	private Complex[] reg;

	/**
	 * 
	 * Initialises to state 0 (|000...0>) with amplitude 1
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
	 * Default constructor. Initialises to state 0 (|000...0>) with amplitude 1
	 * 
	*/
	public ARegister() {
		this(32);
	}	
		
	/**
	 * 
	 * Constructor for array-based register
	 * 
	 * @param numQubits	number of qubits
	 * @param state	state on which it's operating
	 */
	public ARegister(int numQ, int state) {
		numQubits = numQ;
		reg = new Complex[(1 << numQubits)];
		reg[state] = Complex.one();
		for (int i = 0; i < reg.length; i++)
			if (!(i == state))
				reg[i] = Complex.zero();
	}

	@Override
	public IRegister clone() {
		ARegister copy = new ARegister(numQubits);
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
		for (int i = 0; i < reg.length; i++)
			if (!reg[i].equals(Complex.zero()))
				set.add(i);
		return set;
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
	protected void removeState(int state) {
		reg[state] = Complex.zero();
	}

}