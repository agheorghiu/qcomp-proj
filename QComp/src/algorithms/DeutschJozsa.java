package algorithms;

import gates.Hadamard;

import java.util.ArrayList;
import java.util.Random;

import operators.NQOperator;
import operators.Operator;
import operators.OperatorFactory;
import operators.Projector;
import representation.IRegister;
import representation.QRegister;
import representation.Tuple;

/**
 * 
 * Deutsch-Jozsa implementation
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class DeutschJozsa implements Algorithm {
	
	private IRegister reg;
	private int numQubits;
	
	public DeutschJozsa(int numQubits) {
		this.reg = new QRegister(1);
		this.numQubits = numQubits;
	}
	
	@Override
	public void run() {
		OperatorFactory factory = new OperatorFactory(reg);
		Hadamard h = (Hadamard)factory.makeOperator("Hadamard");
		Projector p = new Projector(reg);
		NQOperator function = (NQOperator)getRandomOperator();
		
		// apply H to all qubits
		for (int i = 0; i <= numQubits; i++) {
			h.setIndex(i);
			h.apply();
		}
		
		// apply the function operator
		function.apply();
		
		// apply H to qubits 1 through n
		for (int i = 1; i <= numQubits; i++) {
			h.setIndex(i);
			h.apply();
		}
		
		// measure all n qubits
		for (int i = 1; i <= numQubits; i++) {
			p.setIndex(i);
			if (p.apply()) {
				System.out.println("Function is balanced");
				return;
			}
		}
		
		System.out.println("Function is constant");
		return;
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
	 * Returns the balanced badass operator
	 * 
	 * @return	returns the balanced badass operator
	 */
	private Operator balancedOperator() {
		ArrayList<Tuple<String, Tuple<Integer, Integer>>> tuples =
			new ArrayList<Tuple<String, Tuple<Integer, Integer>>>();
		
		// we assume that qubit y is the 0 indexed qubit in our register
		
		for (int i = 1; i <= numQubits; i++) {
			// putting CNOTs together with control i and target 0
			tuples.add(new Tuple<String, Tuple<Integer, Integer>>("CNOT", new Tuple<Integer, Integer>(i, 0)));
		}
		
		return new NQOperator(reg, tuples);
	}
	
	/**
	 * 
	 * Returns the constant unit operator
	 * 
	 * @return	returns the unit gate
	 */
	private Operator constantOperator() {
		return new NQOperator(reg, numQubits);
	}
	
}
