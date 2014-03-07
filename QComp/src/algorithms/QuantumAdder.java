package algorithms;

import gates.CNOT;
import operators.Operator;
import operators.OperatorFactory;
import operators.Projector;
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
	@SuppressWarnings("unused")
	private int operand1, operand2;
	
	/**
	 * 
	 * Number of qubits for each operand
	 * 
	 */
	private int numQubits;
	
	/**
	 * 
	 * Index of the next available qubit
	 * 
	 */
	private int freeQubitIdx;
	
	/**
	 * 
	 * Factory for producing gates/operators
	 * 
	 */
	private OperatorFactory factory;
	
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
		this.freeQubitIdx = 2 * numQubits;
		this.reg = new QRegister(operand1 + (operand2 << numQubits));
		System.out.println(reg);
		this.factory = new OperatorFactory(reg);
	}
	
	@Override
	public void run() {
		Operator singleAdder;
		int prevCarry = nextQubit();
		System.out.println("FOR LOOP");
		for (int i = 0; i < numQubits; i ++) {
			// add x_i and y_i
			int firstCarry = nextQubit();
			singleAdder = SQubitAdder(i, i + numQubits, firstCarry);
			singleAdder.apply();
			System.out.println(reg);
			// add previous carry to the sum
			int secondCarry = nextQubit();
			singleAdder = SQubitAdder(prevCarry, i + numQubits, secondCarry);
			singleAdder.apply();
			System.out.println(reg);
			// add 2 carries together
			singleAdder = SQubitAdder(secondCarry, firstCarry, nextQubit());
			singleAdder.apply();
			prevCarry = firstCarry;
			System.out.println(reg);
		}

		// recover result
		int result = 0;
		Projector p = new Projector(reg);
		for (int i = 0; i < numQubits; i++) {
			p.setIndex(i + numQubits);
			result += (p.apply() ? (1 << i) : 0);
		}
		p.setIndex(prevCarry);
		result += (p.apply() ? (1 << numQubits) : 0);
		// print result
		System.out.println("Result of quantum adder: " + result);
		System.out.println(reg);
	}
	
	private int nextQubit() {
		int idx = freeQubitIdx;
		freeQubitIdx++;
		return idx;
	}

	/**
	 * 
	 * Add 2 qubits with a carry
	 * 
	 * @param first	first qubit
	 * @param second	second qubit
	 * @param zero	zero qubit (which becomes carry)
	 * @return	operator which adds 2 qubits with carry
	 */
	private Operator SQubitAdder(final int first, final int second, final int zero) {
		final CNOT op = (CNOT)factory.makeOperator("CNOT");
		return new Operator(reg) {
			@Override
			public void apply() {
				// well isn't that cute... BUT IT'S WROOOOOOOOOOOOONG!
				/*
				op.setIndices(first, zero);
				op.apply();
				op.setIndices(second, zero);
				op.apply();
				op.setIndices(first, second);
				op.apply();
				*/
			}
		};
	}
}
