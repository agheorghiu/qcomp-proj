package gates;

import java.util.ArrayList;

import operators.NQOperator;
import representation.IRegister;
import representation.Triplet;

/**
 * 
 * Class which defines the Toffoli
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class Toffoli extends NQOperator {
	/**
	 * 
	 * Constructor for Toffoli
	 * 
	 * @param reg	register on which this gate operates
	 */
	public Toffoli(IRegister reg) {
		super(reg, new ArrayList<Triplet<String, Integer, Integer>>());
	}

	/**
	 * 
	 * Method for setting the indices for the Toffoli gate
	 * 
	 * @param control1	first control
	 * @param control2	second control
	 * @param target	target qubit
	 */
	public void setIndices(int control1, int control2, int target) {
		super.triplets = new ArrayList<Triplet<String, Integer, Integer>>();
		super.triplets.add(new Triplet<String, Integer, Integer>("CRootNOT", control2, target));
		super.triplets.add(new Triplet<String, Integer, Integer>("CNOT", control1, control2));
		super.triplets.add(new Triplet<String, Integer, Integer>("CIRootNOT", control2, target));
		super.triplets.add(new Triplet<String, Integer, Integer>("CNOT", control1, control2));
		super.triplets.add(new Triplet<String, Integer, Integer>("CRootNOT", control1, target));
	}
}
