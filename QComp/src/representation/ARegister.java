package representation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ARegister implements IRegister{
	
	private int numQ;
	private Complex[] reg;
	
	public ARegister(int numQubits){
		numQ = numQubits;
		reg = new Complex[numQ];
		reg[0] = Complex.one();
	}
	
	public ARegister(int numQubits, int state){
		numQ = numQubits;
		reg = new Complex[numQ];
		reg[state] = Complex.one();
	}

	@Override
	public void setState(int state, Complex amplitude) {
		reg[state] = amplitude.clone();		
	}

	@Override
	public Complex getAmplitude(int state) {
		return reg[state];
	}

	@Override
	public Set<Integer> getStates() {
		Set<Integer> set = new HashSet<Integer>();
		for (int i=0; i<numQ; i++)
			if (!reg[i].equals(Complex.zero()))
				set.add(new Integer(i));
		return set;
	}

	/**
	 * 
	 * Swap bits in binary representation of a number
	 * 
	 * @param state	state for which we swap bits
	 * @param index1	first index to be swapped
	 * @param index2	second index to be swapped
	 * @return	returns integer with bits swapped
	 */
	private int swapBits(int state, int index1, int index2) {
		boolean bit1 = (((1 << index1) & state) > 0);
		boolean bit2 = (((1 << index2) & state) > 0);	
		int newState = state;
		if (bit1)
			newState = newState | (1 << index2);
		else
			if (bit2)
				newState = newState ^ (1 << index2);

		if (bit2)
			newState = newState | (1 << index1);
		else
			if (bit1)
				newState = newState ^ (1 << index1);
		return newState;
	}
	
	@Override
	public void swapQubits(int index1, int index2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Integer> getZeroStates(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getOneStates(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void nullifyStates(List<Integer> states) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void normalise() {
		// TODO Auto-generated method stub
		
	}

}
