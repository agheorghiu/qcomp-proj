package algorithms;

import gates.Hadamard;

import java.util.Set;

import operators.Operator;
import operators.OperatorFactory;
import operators.Projector;
import representation.IRegister;
import representation.QRegister;

public class Grover implements Algorithm {
	
	private IRegister reg;
	private int numQubits;
	private int rootN;
	private int omega;
	
	public Grover(int numQubits, int omega) {
		this.reg = new QRegister(1);
		this.numQubits = numQubits;
		this.rootN = 1 << (numQubits / 2 + numQubits % 2);
		this.omega = omega;
	}

	@Override
	public void run() {
		OperatorFactory factory = new OperatorFactory(reg);
		Hadamard h = (Hadamard)factory.makeOperator("Hadamard");
		Projector p = new Projector(reg);
		Operator function = blackBox(numQubits, omega);
		Operator diffusion = diffusion(numQubits);
		
		// apply H to all qubits
		for (int i = 0; i <= numQubits; i++) {
			h.setIndex(i);
			h.apply();
		}
		
		System.out.println("Initial superposition");
		System.out.println(reg);
		System.out.println();
		
		for (int i = 0; i < rootN; i++) {
			// apply the function operator
			function.apply();

			System.out.println("Applied blackbox");
			System.out.println(reg);
			System.out.println();

			
			// apply H to qubits 1 - numQubits
			for (int j = 1; j <= numQubits; j++) {
				h.setIndex(j);
				h.apply();
			}
			
			diffusion.apply();

			// apply H to qubits 1 - numQubits
			for (int j = 1; j <= numQubits; j++) {
				h.setIndex(j);
				h.apply();
			}
			
			System.out.println("After diffusion");
			System.out.println(reg);
			System.out.println();

		}
		
		// measure qubits 1 - numQubits and reconstruct omega from binary
		int computedOmega = 0;
		for (int i = 1; i <= numQubits; i++) {
			p.setIndex(i);
			if (p.apply())
				computedOmega += (1 << (i - 1));
		}
		
		System.out.println("Computed omega is: " + computedOmega);
	}

	private Operator blackBox(final int n, final int omega) {
		Operator function = new Operator(reg) {
			@Override
			public void apply() {
				Set<Integer> states = reg.getStates();
				for (Integer state : states)
					if (state == omega) {
						reg.setState(state, reg.getAmplitude(state).negated());
						return;
					}				
			}
		};
		return function;
	}
	
	private Operator diffusion(final int n) {
		Operator function = new Operator(reg) {
			@Override
			public void apply() {
				Set<Integer> states = reg.getStates();
				for (Integer state : states)
					if (state != 0 && state != 1) {
						reg.setState(state, reg.getAmplitude(state).negated());
						return;
					}				
			}
		};
		return function;
	}
}
