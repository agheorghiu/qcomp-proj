package gates;
import operators.SQOperator;
import representation.Complex;
import representation.IRegister;

/**
 * 
 * Pauli-Z single gate (operator) implementation
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class PauliZ extends SQOperator {
	
	/**
	 * 
	 * Default constructor initialises matrix representation
	 * 
	 */
	public PauliZ(IRegister reg) {
		super(reg);
		this.opElems[0][0] = Complex.one();
		this.opElems[0][1] = Complex.zero();
		this.opElems[1][0] = Complex.zero();
		this.opElems[1][1] = new Complex(-1, 0);
	}
	
	public String toString() {
		return "PauliZ";
	}

}
