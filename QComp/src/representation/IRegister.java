package representation;
import java.util.List;
import java.util.Set;

/**
 * 
 * Register interface
 * 
 * @author Andru, Charlie, Sam
 *
 */
public interface IRegister { // TODO: change to class? Resolve singleton getInstance method
	
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
	
	/**
	 * 
	 * Returns a list of states which have 0 on position index
	 * 
	 * @param index	index of the qubit which we test if it's 0
	 * @return	returns set of states which have 0 on position index
	 */
	public List<Integer> getZeroStates(int index);
	
	/**
	 * 
	 * Returns a list of states which have 1 on position index
	 * 
	 * @param index	index of the qubit which we test if it's 1
	 * @return	returns set of states which have 1 on position index
	 */
	public List<Integer> getOneStates(int index);
	
	
	/**
	 * 
	 * Sets the amplitude of a set of states to 0 and normalises
	 * 
	 * @param states state which we want to nullify
	 */
	public void nullifyStates(List<Integer> states);
	
	/**
	 * 
	 * Normalises the amplitudes
	 * 
	 */
	public void normalise();
}
