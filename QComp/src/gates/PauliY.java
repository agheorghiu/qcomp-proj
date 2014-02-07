package gates;
import operators.SQOperator;
import representation.Complex;
import representation.IRegister;

/**
 * 
 * Pauli-Y single gate (operator) implementation
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class PauliY extends SQOperator {
	
	/**
	 * 
	 * Default constructor initialises matrix representation
	 * 
	 */
	public PauliY(IRegister reg) {
		super(reg);
		this.opElems[0][0] = Complex.zero();
		this.opElems[0][1] = new Complex(0, -1);
		this.opElems[1][0] = new Complex(0, 1);
		this.opElems[1][1] = Complex.zero();
	}
	
	public String toString() {
		return "PauliY";
	}

}
