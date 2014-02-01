import java.util.Set;

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
	 * Sets the value of the qubit index on which to apply the operator
	 * 
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * 
	 * Change amplitudes for even states
	 * 
	 * @param state
	 */
	private void changeEven(int state) {
		Complex oldAmplitude1 = reg.getAmplitude(state);
		Complex oldAmplitude2 = reg.getAmplitude(state + 1);
		Complex newAmplitude1 = Complex.sum(Complex.multiply(opElems[0][0], oldAmplitude1), 
											Complex.multiply(opElems[0][1], oldAmplitude2));
		Complex newAmplitude2 = Complex.sum(Complex.multiply(opElems[1][0], oldAmplitude1), 
											Complex.multiply(opElems[1][1], oldAmplitude2));
		reg.setState(state, newAmplitude1);
		reg.setState(state + 1, newAmplitude2);
	}
	
	/**
	 * 
	 * Apply the operator on the first qubit (qubit 0 of the register)
	 * 
	 */
	public void applyFirst() {
		Set<Integer> states = reg.getStates();
		for (Integer state : states)
			if (state % 2 == 0)
				changeEven(state);
			else
				if(!states.contains(state - 1))
					changeEven(state - 1);
	}

	/**
	 * 
	 * Applies this operator to the register
	 * 
	 */
	@Override
	public void apply() {
		reg.swapQubits(0, index); // swap target qubit with first qubit
		applyFirst();			  // apply operator to first qubit (which is now the target)
		reg.swapQubits(0, index); // swap back target and first qubit
	}
	
}
