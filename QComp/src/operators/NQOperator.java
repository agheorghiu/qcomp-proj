package operators;

import java.util.ArrayList;
import java.util.List;

import representation.IRegister;
import representation.Tuple;

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
	 * List of tuples specifying gate and the indices on which we apply it
	 * 
	 */
	private List<Tuple<String, Tuple<Integer, Integer>>> tuples;
	
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
	 * @param tuples	list of tuples
	 */
	public NQOperator(IRegister reg, List<Tuple<String, Tuple<Integer, Integer>>> tuples) {
		super(reg);
		this.tuples = tuples;
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
		this.tuples = new ArrayList<Tuple<String, Tuple<Integer, Integer>>>();
	}

	/**
	 * 
	 * Apply method
	 * 
	 */
	@Override
	public void apply() {
		for (Tuple<String, Tuple<Integer, Integer>> tuple : tuples) {
			Operator gate = factory.makeOperator(tuple.fst());	
			if (gate instanceof SQOperator)
				((SQOperator)gate).setIndex(tuple.snd().fst());
			else
				((TQOperator)gate).setIndices(tuple.snd().fst(), tuple.snd().snd());
			gate.apply();
		}
	}
}
