/**
 * 
 * Hadamard single gate (operator) implementation
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class Hadamard extends SQOperator {
	
	/**
	 * 
	 * Default constructor initialises matrix representation
	 * 
	 */
	public Hadamard() {
		double constant = 1 / Math.sqrt(2);
		this.opElems[0][0] = new Complex(constant, 0);
		this.opElems[0][1] = new Complex(constant, 0);
		this.opElems[1][0] = new Complex(constant, 0);
		this.opElems[1][1] = new Complex(-constant, 0);
	}
}
