package operators;
import representation.Complex;
import representation.IRegister;

/**
 * 
 * Abstract operator
 * 
 * @author Andru, Charlie, Sam
 *
 */
public abstract class Operator {
	
	/**
	 * 
	 * Quantum register on which operator is acting
	 * 
	 */
	protected IRegister reg;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param reg	register on which we operate
	 */
	public Operator(IRegister reg) {
		this.reg = reg;
	}
	
	/**
	 * 
	 * Matrix representation of this operator (gate)
	 * 
	 */
	protected Complex[][] opElems;
	
	/**
	 * 
	 * Apply method (applies operator to register)
	 * 
	 */
	public abstract void apply(); 
}