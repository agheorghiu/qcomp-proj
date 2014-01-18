package representation;
/**
 * 
 * Class which defines a quantum bit
 * 
 * @author Andru
 *
 */
public class Qubit {
	private ComplexNumber e1, e2;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param e1	Amplitude of |0> component
	 * @param e2	Amplitude of |1> component
	 */
	public Qubit(ComplexNumber e1, ComplexNumber e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	/**
	 * 
	 * Returns amplitude of |0> component
	 * 
	 * @return
	 */
	public ComplexNumber getFirst() {
		return e1;
	}
	
	/**
	 * 
	 * Returns amplitude of |1> component
	 * 
	 * @return
	 */
	public ComplexNumber getSecond() {
		return e2;
	}
	
	/**
	 * 
	 * Checks if qubit is 1
	 * 
	 * @return
	 */
	public boolean isOne() {
		return (e2.squaredMagnitude() == 1.0f);
	}
	
	/**
	 * 
	 * Checks if qubit is 0
	 * 
	 * @return
	 */
	public boolean isZero() {
		return (e1.squaredMagnitude() == 1.0f);
	}
	
	/**
	 * 
	 * Return string representation
	 * 
	 * @return
	 */
	public String toString() {
		String str = "";
		str += "(" + e1 + ") * |0> (" + e2 + ") * |1>";
		return str;
	}
}
