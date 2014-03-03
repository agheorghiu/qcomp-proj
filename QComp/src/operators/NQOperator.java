package operators;

import java.util.ArrayList;

import java.util.List;

import representation.IRegister;
import representation.Triplet;

/**
 * 
 * N qubit operator class
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class NQOperator extends Operator {
	
	/**
	 * 
	 * List of triplets specifying gate and the indices on which we apply it
	 * 
	 */
	private List<Triplet<String, Integer, Integer>> triplets;
	
	/**
	 * 
	 * Factory for making gates
	 * 
	 */
	private OperatorFactory factory;

	/**
	 * 
	 * Constructor
	 * 
	 * @param reg	register on which we're operating
	 * @param triplets	list of triplets
	 */
	public NQOperator(IRegister reg, List<Triplet<String, Integer, Integer>> triplets) {
		super(reg);
		this.triplets = triplets;
		this.factory = new OperatorFactory(reg);
	}
	
	/**
	 * 
	 * Constructor for n-qubit unit matrix
	 * 
	 * @param reg	register on which we're operating
	 * @param numQubits	number of qubits on which we're acting
	 */
	public NQOperator(IRegister reg, int numQubits) {
		super(reg);
		this.triplets = new ArrayList<Triplet<String, Integer, Integer>>();
	}

	/**
	 * 
	 * Apply method
	 * 
	 */
	@Override
	public void apply() {
		for (Triplet<String, Integer, Integer> triplet : triplets) {
			Operator gate = factory.makeOperator(triplet.fst());	
			if (gate instanceof SQOperator)
				((SQOperator)gate).setIndex(triplet.snd());
			else
				((TQOperator)gate).setIndices(triplet.snd(), triplet.trd());
			gate.apply();
		}
	}
}
