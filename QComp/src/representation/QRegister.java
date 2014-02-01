package representation;
import java.util.HashMap;
import java.util.Set;

/**
 * 
 * Implementation of quantum register as a singleton class (single global instance of this register)
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class QRegister implements RegisterInterface {
	
	/**
	 * 
	 * Number of qubits limited by integer
	 * 
	 */
	private final int numQubits = 32;
	
	/**
	 * 
	 * Register implemented as hash map
	 * 
	 */
	private HashMap<Integer, Complex> register = new HashMap<Integer, Complex>();
	
	/**
	 * 
	 * Singleton instance of register
	 * 
	 */
	private static final QRegister instance = new QRegister();
	
	/**
	 * 
	 * Default constructor. Initializes to state 0 (|000...0>) with amplitude 1
	 * 
	 */
	private QRegister() {
		register.put(0, Complex.one());
	}
	
	/**
	 * 
	 * Change amplitude of a certain state
	 * 
	 * @param state	state for which we change the amplitude
	 * @param amplitude	new amplitude for state
	 */
	public void setState(int state, Complex amplitude) {
		register.put(state, amplitude);
	}

	/**
	 * 
	 * Return the amplitude for a given state
	 * 
	 * @param state	state for which we want to find the amplitude
	 * @return	returns the amplitude of the state
	 */
	public Complex getAmplitude(int state) {
		if (register.containsKey(state))
			return register.get(state);
		else
			return new Complex(0, 0);
	}
	
	/**
	 * 
	 * Returns binary representation of a given state
	 * 
	 * @param state	state for which we want to output binary representation
	 * @return	returns binary representation of state
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
	 * Method for returning the set of states with non-zero amplitudes 
	 * 
	 * @return
	 */
	public Set<Integer> getStates() {
		return register.keySet();
	}
	
	/**
	 * 
	 * Method which returns the singleton instance of this class
	 * 
	 * @return	returns the singular (only) instance of this class
	 */
	public static QRegister getInstance() {
		return instance;
	}
	
	/**
	 * 
	 * Swap bits in binary representation of a number
	 * 
	 * @param state	state for which we swap bits
	 * @param index1	first index to be swapped
	 * @param index2	second index to be swapped
	 * @return	returns integer with bits swapped
	 */
	private int swapBits(int state, int index1, int index2) {
		boolean bit1 = (((1 << index1) & state) > 0);
		boolean bit2 = (((1 << index2) & state) > 0);	
		int newState = state;
		if (bit1)
			newState = newState | (1 << index2);
		else
			if (bit2)
				newState = newState ^ (1 << index2);

		if (bit2)
			newState = newState | (1 << index1);
		else
			if (bit1)
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
	public void swapQubits(int index1, int index2) {
		HashMap<Integer, Complex> clone = new HashMap<Integer, Complex>(); 
		for (Integer state : getStates()) {
			int swapped = swapBits(state, index1, index2);
			clone.put(swapped, getAmplitude(state));
		}
		register = clone;
	}
	
	/**
	 * 
	 * Returns string representation of the register
	 * 
	 */
	public String toString() {
		String str = "";
		for (Integer key : register.keySet())
			str += toBinary(key) + ": " + getAmplitude(key) + "\n";
		return str;			
	}
}