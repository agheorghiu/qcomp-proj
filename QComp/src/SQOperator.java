/**
 * 
 * Single qubit operator
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class SQOperator extends Operator {
	
	/**
	 * 
	 * Index of the qubit on which this operator is applied
	 * 
	 */
	private int index;
	
	/**
	 * 
	 * Constructor which takes the matrix representation of this operator (gate)
	 * 
	 * @param opElems	Matrix representation of the operator (gate)
	 */
	public SQOperator(Complex[][] opElems) {
		this.opElems = opElems;
	}
	
	/**
	 * 
	 * Default constructor (identity matrix representation)
	 * 
	 */
	public SQOperator() {
		Complex[][] matrix = { {Complex.one(), Complex.zero()}, 
							   {Complex.zero(), Complex.one()}
							 };
		this.opElems = matrix;
	}

	/**
	 * 
	 * 
	 * 
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * 
	 * Applies this operator to the register
	 * 
	 */
	@Override
	public void apply() {
		
	}
}
