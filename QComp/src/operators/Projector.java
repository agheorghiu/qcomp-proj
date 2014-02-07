package operators;

import java.util.List;
import java.util.Random;

import representation.IRegister;

public class Projector {
	
	/**
	 * 
	 * Register object
	 * 
	 */
	private IRegister reg;
	
	/**
	 * 
	 * Index of qubit which we want to measure
	 * 
	 */
	private int index;
	
	public Projector(IRegister reg) {
		this.reg = reg;
	}
	
	/**
	 * 
	 * Set the value of the index
	 * 
	 * @param index	index of qubit which we want to measure
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	private double getZeroProbability() {
		double probability = 0.0;
		for (Integer state : reg.getZeroStates(index))
				probability += reg.getAmplitude(state).modS();			
		return probability;
	}
	
	/**
	 * 
	 * Applies the projector (measurement) to the quantum register
	 * 
	 * @return	returns true if measurement output is 1, or 0 otherwise
	 */
	public boolean apply() {
		double probZero = getZeroProbability();
		Random gen = new Random();
		double rand = gen.nextDouble();
		List<Integer> nullifiableStates;
		boolean result;
		if (rand <= probZero) {
			nullifiableStates = reg.getOneStates(index);
			result = false;
		}
		else {
			nullifiableStates = reg.getZeroStates(index);
			result = true;
		}
		reg.nullifyStates(nullifiableStates);
		return result;
	}
}
