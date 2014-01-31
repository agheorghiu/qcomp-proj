/**
 * 
 * Two qubit operator
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class TQOperator extends Operator {
	
	/**
	 * 
	 * Indices for control qubit and target qubit
	 * 
	 */
	private int control, target;
	
	/**
	 * 
	 * Constructor which receives matrix representation
	 * 
	 * @param opElems	the matrix representation
	 */
	public TQOperator(Complex[][] opElems) {
		this.opElems = opElems;
	}
	
	/**
	 * 
	 * Default constructor (identity matrix)
	 * 
	 */
	public TQOperator() {
		Complex[][] matrix = new Complex[4][4];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (i == j)
					matrix[i][j] = Complex.one();
				else
					matrix[i][j] = Complex.zero();
	}
	
	/**
	 * 
	 * Sets the control and target indices
	 * 
	 * @param control	control index
	 * @param target	target index
	 */
	public void setIndices(int control, int target) {
		this.control = control;
		this.target = target;
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
