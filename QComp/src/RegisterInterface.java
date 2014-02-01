import java.util.Set;


public interface RegisterInterface {
	
	/**
	 * 
	 * Change amplitude of a certain state
	 * 
	 * @param state	state for which we change the amplitude
	 * @param amplitude	new amplitude for state
	 */
	public void setState(int state, Complex amplitude);

	/**
	 * 
	 * Return the amplitude for a given state
	 * 
	 * @param state	state for which we want to find the amplitude
	 * @return	returns the amplitude of the state
	 */
	public Complex getAmplitude(int state);

	/**
	 * 
	 * Method for returning the set of states with non-zero amplitudes 
	 * 
	 * @return
	 */
	public Set<Integer> getStates();
	
	/**
	 * 
	 * Swap qubits in the register (for all states in the superposition)
	 * 
	 * @param index1	first index to be swapped
	 * @param index2	second index to be swapped
	 */
	public void swapQubits(int index1, int index2);

}
