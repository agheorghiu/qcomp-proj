package operators;
import java.util.Set;

import representation.Complex;

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
		this.opElems = new Complex[4][4];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (i == j)
					this.opElems[i][j] = Complex.one();
				else
					this.opElems[i][j] = Complex.zero();
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
	 * Change amplitudes for 4k states. If current state is a multiple of 4, then we update amplitudes for this state and the next 3 ones.
	 * 
	 * @param state
	 */
	private void change4k(int state) {
		Complex[] oldAmplitudes = new Complex[4];
		Complex[] newAmplitudes = new Complex[4];
		for (int i = 0; i < 4; i++)
			oldAmplitudes[i] = reg.getAmplitude(state + i);
		for (int i = 0; i < 4; i++) {
			newAmplitudes[i] = Complex.zero();
			for (int j = 0; j < 4; j++)
				newAmplitudes[i].add(Complex.multiply(opElems[i][j], oldAmplitudes[j]));
		}
		for (int i = 0; i < 4; i++)
			reg.setState(state + i, newAmplitudes[i]);
	}
	
	/**
	 * 
	 * Apply the operator on the first 2 qubits (qubit 0 is control and qubit 1 is target)
	 * 
	 */
	public void applyFirst() {
		Set<Integer> states = reg.getStates();
		for (Integer state : states)
			if (state % 4 == 0)
				change4k(state);
			else {
				int rem = state % 4;
				boolean isDone = false;
				for (int i = 0; i < rem; i++)
					if (states.contains(state - rem + i)) {
						isDone = true;
						break;
					}
				if(!isDone)
					change4k(state - rem);
			}
	}

	/**
	 * 
	 * Applies this operator to the register
	 * 
	 */
	@Override
	public void apply() { // TODO: Double check the logic here!!!
		reg.swapQubits(0, target);       // swap target qubit with first qubit
		if (control == 0)                // if control is 0, then we just swapped it with target, so control is now target
			reg.swapQubits(1, target);
		else
			reg.swapQubits(1, control);  // swap control qubit with second qubit
		applyFirst();			         // apply operator to first qubit (which is now the target)
		reg.swapQubits(0, target);       // swap back target qubit
		if (control == 0)                // if control was 0, then control is actually target
			reg.swapQubits(1, target);
		else
			reg.swapQubits(1, control);  // swap back control qubit
	}

}