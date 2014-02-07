package gates;

import operators.TQOperator;
import representation.Complex;
import representation.IRegister;

/**
 * 
 * Class which defines the controlled-NOT operator (gate)
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class CNOT extends TQOperator {
	
	/**
	 * 
	 * Default constructor initialises matrix representation
	 * 
	 */
	public CNOT(IRegister reg) {
		super(reg);
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				this.opElems[i][j] = Complex.zero();
		this.opElems[0][0] = Complex.one();
		this.opElems[1][1] = Complex.one();
		this.opElems[2][3] = Complex.one();
		this.opElems[3][2] = Complex.one();
	}

	public String toString() {
		return "CNOT";
	}

}
