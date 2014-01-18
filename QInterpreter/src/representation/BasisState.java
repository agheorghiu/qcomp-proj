package representation;

import java.util.HashMap;
import java.util.Set;

public class BasisState {
	private int numQubits;
	private HashMap<Integer, Integer> ones;
	private ComplexNumber amplitude;
	
	public BasisState(int numQubits, ComplexNumber amplitude) {
		this.numQubits = numQubits;
		this.amplitude = amplitude;
		ones = new HashMap<Integer, Integer>();
	}
	
	public void put(int qubit, int pos) {
		ones.put(pos, qubit);
	}
	
	public int whichQubit(int pos) {
		if (ones.get(pos) != null)
			return ones.get(pos);
		return 0;
	}
	
	public int getNumQubits() {
		return numQubits;
	}
	
	public ComplexNumber getAmplitude() {
		return amplitude;
	}
	
	public void addAmplitude(ComplexNumber amplitude) {
		this.amplitude.add(amplitude);
	}
	
	public void timesConstant(double constant) {
		amplitude.timesConstant(constant);
	}
	
	public void timesComplex(ComplexNumber c) {
		amplitude.mul(c);
	}
	
	public double getProbability() {
		return amplitude.squaredMagnitude();
	}
	
	public Set<Integer> getKeys() {
		return ones.keySet();
	}
	
	public BasisState clone() {
		BasisState clone = new BasisState(numQubits, new ComplexNumber(amplitude.getRe(), amplitude.getIm()));
		for (Integer i : getKeys())
			clone.put(ones.get(i), i);
		return clone;
	}
	
	public boolean equals(Object o) {
		try {
			BasisState b = (BasisState)o;
			for (Integer i : ones.keySet()) {
				if (!b.ones.containsKey(i) || b.ones.get(i) != ones.get(i))
					return false;
			}
			for (Integer i : b.ones.keySet()) {
				if (!ones.containsKey(i) || b.ones.get(i) != ones.get(i))
					return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
