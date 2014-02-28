package algorithms;

import gates.CNOT;
import operators.Operator;
import operators.OperatorFactory;
import representation.IRegister;
import representation.QRegister;

/**
 * 
 * Implementation of a quantum adder
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class QuantumAdder implements Algorithm { // TODO: EVERYTHING!

	/**
	 * 
	 * Register on which we're operating
	 * 
	 */
	private IRegister reg;
	
	/**
	 * 
	 * The two operands we are adding
	 * 
	 */
	private int operand1, operand2, numQubits;
	
	/**
	 * 
	 * Factory for producing gates/operators
	 * 
	 */
	private OperatorFactory factory = new OperatorFactory(reg);
	
	/**
	 * 
	 * Constructor for quantum adder algorithm
	 * 
	 * @param operand1	first operand
	 * @param operand2	second operand
	 */
	public QuantumAdder(int operand1, int operand2, int numQubits) {
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.numQubits = numQubits;
		reg = new QRegister(operand1 + (operand2 << numQubits));
	}
	
	@Override
	public void run() {
		Operator singleAdder;
		int prevCarry = 2 * numQubits;
		int currentCarry = 2 * numQubits + 1;
		int anotherCarry = 2 * numQubits + 2;
		for (int i = 0; i < numQubits; i ++) {
			// add x_i and y_i
			singleAdder = SQubitAdder(i, i + numQubits, currentCarry);
			// add previous carry to the sum
			singleAdder = SQubitAdder(prevCarry, i + numQubits, anotherCarry);
			// reuse x_i, make it 0
			
		}
		
	}

	private Operator SQubitAdder(final int first, final int second, final int zero) {
		final CNOT op = (CNOT)factory.makeOperator("CNOT");
		return new Operator(reg) {
			@Override
			public void apply() {
				op.setIndices(first, zero);
				op.apply();
				op.setIndices(second, zero);
				op.apply();
				op.setIndices(first, second);
				op.apply();
			}
		};
	}
}
