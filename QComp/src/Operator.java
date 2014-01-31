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
	protected QRegister reg = QRegister.getInstance();
	
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