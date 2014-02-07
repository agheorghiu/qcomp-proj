package gates;
import operators.SQOperator;
import representation.Complex;
import representation.IRegister;

/**
 * 
 * Pauli-X single gate (operator) implementation
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class PauliX extends SQOperator {
	
	/**
	 * 
	 * Default constructor initialises matrix representation
	 * 
	 */
	public PauliX(IRegister reg) {
		super(reg);
		this.opElems[0][0] = Complex.zero();
		this.opElems[0][1] = Complex.one();
		this.opElems[1][0] = Complex.one();
		this.opElems[1][1] = Complex.zero();
	}
	
	public String toString() {
		return "PauliX";
	}

}