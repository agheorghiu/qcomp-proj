import java.util.HashMap;
import java.util.Set;

/**
 * 
 * Implementation of quantum register as a singleton class (single global instance of this register)
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class QRegister {
	
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
		String str = Integer.toBinaryString(state);
		int len = str.length();
		for (int i = len; i <= numQubits; i++)
			str += "0";
		return str; // TODO: reverse this!
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
	 * Returns string representation of the register
	 * 
	 */
	public String toString() {
		String str = "";
		for (Integer key : register.keySet())
			str += toBinary(key) + "---" + getAmplitude(key) + "\n";
		return str;			
	}
}