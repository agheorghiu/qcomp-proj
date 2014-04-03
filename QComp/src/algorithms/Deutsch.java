package algorithms;

import gates.Hadamard;

import java.util.Random;

import operators.Operator;
import operators.OperatorFactory;
import operators.Projector;
import operators.TQOperator;
import representation.IRegister;
import representation.QRegister;

/**
 * 
 * Implementation of Deutsch's algorithm
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class Deutsch implements Algorithm {
	
	/**
	 * 
	 * Register on which we operate
	 * 
	 */
	private IRegister reg;

	/**
	 * 
	 * Factory for making gates
	 * 
	 */
	private OperatorFactory factory;
	
	/**
	 * 
	 * Constructor for Deutsch's algorithm
	 * 
	 */
	public Deutsch() {
		reg = new QRegister(1);
		factory = new OperatorFactory(reg);
	}

	@Override
	public void run() {
		Hadamard h = (Hadamard)factory.makeOperator("Hadamard");
		Projector p = new Projector(reg);
		TQOperator function = (TQOperator)getRandomOperator();
		
		// apply H to qubits indexed 0 and 1
		h.setIndex(0);
		h.apply();
		
		h.setIndex(1);
		h.apply();

		// apply the operator
		function.setIndices(1, 0);
		function.apply();
		
		// apply H to qubit 1
		h.setIndex(1);
		h.apply();
		
		// perform measurement on first qubit and output result
		p.setIndex(1);
		if (p.apply())
			System.out.println("Function is balanced");
		else
			System.out.println("Function is constant");

	}
	
	/**
	 * 
	 * Returns randomly either a balanced or a constant operator
	 * 
	 * @return	balanced or constant operator function
	 */
	private Operator getRandomOperator() {
		Random gen = new Random();
		if (gen.nextBoolean()) {
			System.out.println("Random generator picked balanced operator");
			return balancedOperator();
		}
		else {
			System.out.println("Random generator picked constant operator");
			return constantOperator();
		}
	}
	
	/**
	 * 
	 * Returns the balanced operator CNOT
	 * 
	 * @return	returns the CNOT gate
	 */
	private Operator balancedOperator() {
		return factory.makeOperator("CNOT");
	}
	
	/**
	 * 
	 * Returns the constant unit operator
	 * 
	 * @return	returns the unit gate
	 */
	private Operator constantOperator() {
		return new TQOperator(reg);
	}
	
}
