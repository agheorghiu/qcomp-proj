package representation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * Implementation of quantum register
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class QRegister implements IRegister {
	
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
	 * Default constructor. Initializes to state 0 (|000...0>) with amplitude 1
	 * 
	 */
	public QRegister() {
		register.put(0, Complex.one());
	}
	
	/**
	 * 
	 * State constructor. Initializes to state binary representation of state
	 * 
	 */
	public QRegister(int state) {
		register.put(state, Complex.one());
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
	 * Method for returning (a copy of) the set of states with non-zero amplitudes 
	 * 
	 * @return
	 */
	public Set<Integer> getStates() {
		Set<Integer> set = new HashSet<Integer>();
		for (Integer state : register.keySet())
			set.add(new Integer(state));
		return set;
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
	 * Normalises the states
	 * 
	 */
	@Override
	public void normalise() {
		double normaliser = 0.0;
		for (Integer state : getStates())
			normaliser += register.get(state).modS();
		normaliser = Math.sqrt(normaliser);
		Complex divider = new Complex(1.0 / normaliser, 0);
		for (Integer state : getStates())
			register.put(state, Complex.multiply(register.get(state), divider));		
	}
	
	/**
	 * 
	 * Sets the amplitude of a set of states to 0 and normalises
	 * 
	 * @param states state which we want to nullify
	 */
	@Override
	public void nullifyStates(List<Integer> states) {
		for (Integer state : states)
			register.remove(state);
		normalise();
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

}