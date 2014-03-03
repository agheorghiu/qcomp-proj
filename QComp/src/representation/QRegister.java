package representation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * Implementation of quantum register
 * 
 * @author Andru, Charlie, Sam
 * 
 */
public class QRegister extends IRegister {

	/**
	 * 
	 * Register implemented as hash map
	 * 
	 */
	private HashMap<Integer, Complex> register = new HashMap<Integer, Complex>();

	/**
	 * 
	 * Default constructor. Initialises to state 0 (|000...0>) with amplitude 1
	 * 
	 */
	public QRegister() {
		register.put(0, Complex.one());
	}

	/**
	 * 
	 * State constructor. Initialises to state  0 (|000...0>) binary representation of state
	 * 
	 * @param state
	 *            state for which the amplitude is to be set to one.
	 */
	public QRegister(int state) {
		register.put(state, Complex.one());
	}

	@Override
	public QRegister clone() {
		QRegister copy = new QRegister();
		copy.register.remove(0);
		for (Integer state : register.keySet())
			copy.setState(state, getAmplitude(state));
		return copy;
	}

	@Override
	public void setState(int state, Complex amplitude) {
		register.put(state, amplitude);
	}

	@Override
	public Complex getAmplitude(int state) {
		if (register.containsKey(state))
			return register.get(state);
		else
			return new Complex(0, 0);
	}

	@Override
	public Set<Integer> getStates() {
		Set<Integer> set = new HashSet<Integer>();
		for (Integer state : register.keySet())
			set.add(state);
		return set;
	}

	@Override
	public void swapQubits(int index1, int index2) {
		HashMap<Integer, Complex> clone = new HashMap<Integer, Complex>();
		for (Integer state : getStates()) {
			int swapped = swapBits(state, index1, index2);
			clone.put(swapped, getAmplitude(state));
		}
		register = clone;
	}

	@Override
	protected void removeState(int state) {
		register.remove(state);
	}

}