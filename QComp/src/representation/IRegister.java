package representation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import operators.Projector;

/**
 * 
 * Register interface
 * 
 * @author Andru, Charlie, Sam
 *
 */
public abstract class IRegister {

	/**
	 * 
	 * Number of qubits to be used
	 * 
	 */
	protected int numQubits = 32;


	/**
	 * 
	 * Change amplitude of a certain state
	 * 
	 * @param state	state for which we change the amplitude
	 * @param amplitude	new amplitude for state
	 */
	public abstract void setState(int state, Complex amplitude);

	/**
	 * 
	 * Return the amplitude for a given state
	 * 
	 * @param state	state for which we want to find the amplitude
	 * @return	returns the amplitude of the state
	 */
	public abstract Complex getAmplitude(int state);

	/**
	 * 
	 * Method for returning the set of states with non-zero amplitudes 
	 * 
	 * @return
	 */
	public abstract Set<Integer> getStates();
	
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
	protected int swapBits(int state, int index1, int index2) {
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


	/**
	 * 
	 * Swap qubits in the register (for all states in the superposition)
	 * 
	 * @param index1	first index to be swapped
	 * @param index2	second index to be swapped
	 */
	public abstract void swapQubits(int index1, int index2);

	/**
	 * 
	 * Returns a list of states which have 0 on position index
	 * 
	 * @param index	index of the qubit which we test if it's 0
	 * @return	returns set of states which have 0 on position index
	 */
	public List<Integer> getZeroStates(int index) {
		List<Integer> list = new ArrayList<Integer>();
		for (Integer state : getStates()) {
			if ((state & (1 << index)) == 0)
				list.add(state);
		}
		return list;
	}


	/**
	 * 
	 * Returns a list of states which have 1 on position index
	 * 
	 * @param index	index of the qubit which we test if it's 1
	 * @return	returns set of states which have 1 on position index
	 */
	public List<Integer> getOneStates(int index) {
		List<Integer> list = new ArrayList<Integer>();
		for (Integer state : getStates()) {
			if ((state & (1 << index)) != 0)
				list.add(state);
		}
		return list;
	}
	
	/**
	 * 
	 * Returns binary representation of a given state
	 * 
	 * @param state
	 *            state for which we want to output binary representation
	 * @return returns binary representation of state
	 */
	protected String toBinary(int state) {
		String str = new StringBuilder(Integer.toBinaryString(state)).reverse().toString();
		int len = str.length();
		for (int i = len; i <= numQubits; i++)
			str += "0";
		return str;
	}
	
	/**
	 * 
	 * Sets the amplitude of a set of states to 0 and normalises
	 * 
	 * @param states state which we want to nullify
	 */
	public void nullifyStates(List<Integer> states) {
		for (Integer state : states)
			removeState(state);
		normalise();
	}	

	/**
	 * 
	 * Remove a state from the register
	 * 
	 * @param state	state to be removed
	 */
	protected abstract void removeState(int state);

	/**
	 * 
	 * Normalises the amplitudes
	 * 
	 */
	public void normalise() {
		double normaliser = 0.0;
		for (Integer state : getStates())
			normaliser += getAmplitude(state).modS();
		normaliser = Math.sqrt(normaliser);
		Complex divider = new Complex(1.0 / normaliser, 0);
		for (Integer state : getStates())
			setState(state, Complex.multiply(getAmplitude(state), divider));
	}
	
	/**
	 * 
	 * Set a specific qubit to either 1 or 0.
	 * We first measure the qubit. If it's what we want it to be, leave it unchanged,
	 * otherwise flip it.
	 * 
	 * @param index	index of the qubit we're trying to set
	 * @param value	value of this qubit (true = 1, false = 0)
	 */
	public void setQubit(int index, boolean value) {
		// perform measurement
		Projector proj = new Projector(this);
		proj.setIndex(index);
		if (proj.apply() == value)
			return;
		// flip qubit on position index
		for (Integer state : getStates()) {
			Complex amplitude = getAmplitude(state);
			setState(state, Complex.zero());
			setState(state ^ (1 << index), amplitude);
		}
	}
	
	@Override
	public String toString() {
		String str = "";
		for (Integer state : getStates()) {
			Complex amplitude = getAmplitude(state);
			if (!amplitude.equals(Complex.zero()))
				str += toBinary(state) + ": " + amplitude + "\n";
		}
		return str;
	}

}