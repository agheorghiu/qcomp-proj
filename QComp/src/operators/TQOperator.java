package operators;

import java.util.Set;

import representation.Complex;
import representation.IRegister;

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
	 * @param opElems the matrix representation
	 * 
	 */
	public TQOperator(IRegister reg, Complex[][] opElems) {
		super(reg);
		this.opElems = opElems;
	}

	/**
	 * 
	 * Constructor which initialises matrix representation to unit matrix
	 * 
	 * @param reg	register on which we operate
	 */
	public TQOperator(IRegister reg) {
		super(reg);
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
	 * @param control
	 *            control index
	 * @param target
	 *            target index
	 */
	public void setIndices(int control, int target) {
		this.control = control;
		this.target = target;
	}

	/**
	 * 
	 * Change amplitudes for 4k states. If current state is a multiple of 4,
	 * then we update amplitudes for this state and the next 3 ones.
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
				newAmplitudes[i].add(Complex.multiply(opElems[i][j],
						oldAmplitudes[j]));
		}
		for (int i = 0; i < 4; i++)
			reg.setState(state + i, newAmplitudes[i]);
	}

	/**
	 * 
	 * Apply the operator on the first 2 qubits (qubit 1 is control and qubit 0
	 * is target)
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
				if (!isDone)
					change4k(state - rem);
			}
	}

	@Override
	public void apply() {
		if (control == 1 && target == 0) {
			applyFirst();
		} else if (control == 0 && target == 1) {
			reg.swapQubits(0, 1);
			applyFirst();
			reg.swapQubits(0, 1);
		} else if (control == 1 && target > 1) {
			reg.swapQubits(0, target);
			applyFirst();
			reg.swapQubits(0, target);
		} else if (control > 1 && target == 0) {
			reg.swapQubits(1, control);
			applyFirst();
			reg.swapQubits(1, control);
		} else if (control == 0 && target > 1){
			reg.swapQubits(0, target);
			reg.swapQubits(1, target);
			applyFirst();
			reg.swapQubits(0, 1);
			reg.swapQubits(1, target);
		} else if (control > 1 && target == 1) {
			reg.swapQubits(0, 1);
			reg.swapQubits(1, control);
			applyFirst();
			reg.swapQubits(0, 1);
			reg.swapQubits(0, control);
		} else {
			reg.swapQubits(1, control);
			reg.swapQubits(0, target);
			applyFirst();
			reg.swapQubits(1, control);
			reg.swapQubits(0, target);
		}
	}

}