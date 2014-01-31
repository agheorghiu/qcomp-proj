import java.util.HashMap;
import java.util.Set;

/**
 * 
 * Implementation of quantum register
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class QRegister {
	
	private final int numQubits = 32;
	private HashMap<Integer, Complex> register = new HashMap<Integer, Complex>();
	private static final QRegister instance = new QRegister();
	
	private QRegister() {}
	
	public void setState(int state, Complex amplitude) {
		register.put(state, amplitude);
	}

	public Complex getAmplitude(int state) {
		if (register.containsKey(state))
			return register.get(state);
		else
			return new Complex(0, 0);
	}
	
	private String toBinary(int state) {
		String str = Integer.toBinaryString(state);
		int len = str.length();
		for (int i = len; i <= numQubits; i++)
			str += "0";
		return str; // TODO: reverse this!
	}
	
	public Set<Integer> getStates() {
		return register.keySet();
	}
	
	public String toString() {
		String str = "";
		for (Integer key : register.keySet())
			str += toBinary(key) + "---" + getAmplitude(key) + "\n";
		return str;			
	}
	
	public static QRegister getInstance() {
		return instance;
	}
}
