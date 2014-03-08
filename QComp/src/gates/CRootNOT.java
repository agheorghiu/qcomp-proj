package gates;

import operators.TQOperator;
import representation.Complex;
import representation.IRegister;

/**
 * 
 * Class which defines the controlled square-root of NOT gate
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class CRootNOT extends TQOperator {

	/**
	 * 
	 * Default constructor initialises matrix representation
	 * 
	 */
	public CRootNOT(IRegister reg) {
		super(reg);
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				this.opElems[i][j] = Complex.zero();
		this.opElems[0][0] = Complex.one();
		this.opElems[1][1] = Complex.one();
		Complex factor = Complex.sum(Complex.one(), Complex.i());
		factor.scale(0.5);
		this.opElems[2][2] = Complex.multiply(factor, Complex.one());
		this.opElems[2][3] = Complex.multiply(factor, Complex.i().negated());
		this.opElems[3][2] = Complex.multiply(factor, Complex.i().negated());
		this.opElems[3][3] = Complex.multiply(factor, Complex.one());
	}

	public String toString() {
		return "CRootNOT";
	}

}
