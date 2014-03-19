package gates;

import operators.TQOperator;
import representation.Complex;
import representation.IRegister;

/**
 * 
 * Controlled RK gate. RK gate is a form of phase gate. It is represented by the
 * unitary matrix: [1 0; 0 e^(2pi * i / 2^k)] for some k.
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class CRK extends TQOperator {

	/**
	 * 
	 * Default constructor initialises matrix representation as unit matrix
	 * 
	 */
	public CRK(IRegister reg) {
		super(reg);
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				this.opElems[i][j] = Complex.zero();
		this.opElems[0][0] = Complex.one();
		this.opElems[1][1] = Complex.one();
		this.opElems[2][2] = Complex.one();
		this.opElems[3][3] = Complex.one();
	}

	/**
	 * 
	 * Set phase parameter. Sets phase as 2*pi / 2^k
	 * 
	 * @param k	phase parameter
	 */
	public void setPhaseParam(int k) {
		double pow2 = (1 << k);
		double phase = 2 * Math.PI / pow2;
		opElems[3][3] = new Complex(Math.cos(phase), Math.sin(phase));
	}

	
	public String toString() {
		return "CRK";
	}

}
