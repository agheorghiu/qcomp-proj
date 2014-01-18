package virtualMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import representation.BasisState;
import representation.BinaryArray;
import representation.ComplexNumber;
import representation.FLOperator;
import representation.TLOperator;

public class GlobalMemoryState {
	private ArrayList<BasisState> basisStates;
	private int numQubits;
	
	public GlobalMemoryState(int numQubits) {
		this.numQubits = numQubits;
		basisStates = new ArrayList<BasisState>();
		basisStates.add(new BasisState(numQubits, new ComplexNumber(1, 0)));
	}
	
	public void measure(int[] indexes) {
		HashMap<BinaryArray, Double> probabilities = new HashMap<BinaryArray, Double>();
		Random generator = new Random();
		double randomNumber = generator.nextDouble();
		ArrayList<BasisState> newBasisStates = new ArrayList<BasisState>();
		double norm = 0;
	
		for (BasisState state : basisStates) {
			BinaryArray b = new BinaryArray(indexes.length);
			double currentProbability = 0;
			for (int i = 0; i < indexes.length; i++)
				if (state.whichQubit(indexes[i]) == 1) {
					b.flipIndex(i);
				}
			if (probabilities.get(b) != null)
				currentProbability = probabilities.get(b);
			probabilities.put(b, currentProbability + state.getAmplitude().squaredMagnitude());
		}
		
		BinaryArray chosenArray = null;
		for (BinaryArray b : probabilities.keySet()) {
			double currentProbability = probabilities.get(b);
			if (randomNumber <= currentProbability) {
				chosenArray = b;
				break;
			}
			else
				randomNumber -= currentProbability;
		}
		
		for (BasisState state : basisStates) {
			BinaryArray b = new BinaryArray(indexes.length);
			for (int i = 0; i < indexes.length; i++)
				if (state.whichQubit(indexes[i]) == 1)
					b.flipIndex(i);
			if (b.equals(chosenArray)) {
				norm += state.getProbability();
				newBasisStates.add(state);
			}
		}
		norm = Math.sqrt(norm);
		
		for (BasisState state : newBasisStates) {
			state.timesConstant((1.0 / norm));
		}
		
		basisStates = newBasisStates;
	}
	
	public void measure(int addr, int size) {
		int[] indexes = new int[size];
		for (int i = 0; i < size; i++)
			indexes[i] = addr + i;
		measure(indexes);
	}
	
	public void swap(int src, int dest, int size) {
		for (BasisState state : basisStates) {
			for (int i = 0; i < size; i++) {
				int aux = state.whichQubit(i + src);
				state.put(state.whichQubit(i + dest), i + src);
				state.put(aux, i + dest);
			}
		}
	}
	
	public void reverse(int src, int size) {
		for (BasisState state : basisStates) {
			for (int i = 0; i < size / 2; i++) {
				int aux = state.whichQubit(i + src);
				state.put(state.whichQubit(src + size - 1 - i), i + src);
				state.put(aux, src + size - 1 - i);
			}
		}		
	}
	
	public void erase(int dest, int size) {
		for (BasisState state : basisStates) {
			for (int i = 0; i < size; i++) {
				state.put(0, i + dest);
			}
		}		
	}
	
	public void loadstore(int src, int dest, int size) {
		measure(dest, size);
		erase(dest, size);
		swap(src, dest, size);
	}
	
	public void loadstore(int[] src, int dest, int size) {
		measure(dest, size);
		erase(dest, size);
		for (BasisState state : basisStates) {
			for (int i = 0; i < src.length; i++)
				state.put(src[i], i + dest);
		}	
	}
	
	public int[] getCollapsedMemLocation(int addr, int size) {
		int[] collapsed = new int[size];
		measure(addr, size);
		for (BasisState state : basisStates) {
			for (int i = 0; i < size; i++)
				collapsed[i] = state.whichQubit(i + addr);
		}
		return collapsed;
	}
	
	public void showMemLocation(int addr, int size) {
		System.out.println(basisStates.size());
		for (BasisState state : basisStates) {
			System.out.print("A: " + state.getAmplitude() + " B:");
			for (int i = 0; i < size; i++)
				System.out.print(state.whichQubit(i + addr));
			System.out.println();
		}
	}
	
	public void applyTLOperator(TLOperator op) {
		ArrayList<BasisState> newBasisStates = new ArrayList<BasisState>();
		for (BasisState state : basisStates) {
			if (state.whichQubit(state.getNumQubits() - 1) == 0) {
				BasisState neighborState1 = state.clone();
				BasisState neighborState2 = state.clone();
				neighborState2.put(1, state.getNumQubits() - 1);
				neighborState1.timesComplex(op.getA());
				neighborState2.timesComplex(op.getC());
				boolean add1 = true, add2 = true;

				for (BasisState b : newBasisStates) {
					if (b.equals(neighborState1)) {
						b.addAmplitude(neighborState1.getAmplitude());
						add1 = false;
					}
					if (b.equals(neighborState2)) {
						b.addAmplitude(neighborState2.getAmplitude());
						add2 = false;
					}
				}

				if (add1)
					newBasisStates.add(neighborState1);
				if (add2)
					newBasisStates.add(neighborState2);
			}
			else {
				BasisState neighborState1 = state.clone();
				BasisState neighborState2 = state.clone();
				neighborState1.put(0, state.getNumQubits() - 1);
				neighborState1.timesComplex(op.getB());
				neighborState2.timesComplex(op.getD());
				boolean add1 = true, add2 = true;

				for (BasisState b : newBasisStates) {
					if (b.equals(neighborState1)) {
						b.addAmplitude(neighborState1.getAmplitude());
						add1 = false;
					}
					if (b.equals(neighborState2)) {
						b.addAmplitude(neighborState2.getAmplitude());
						add2 = false;
					}
				}

				if (add1)
					newBasisStates.add(neighborState1);
				if (add2)
					newBasisStates.add(neighborState2);
			}
		}
		basisStates = newBasisStates;
		removeNullElements();
	}
	
	public void applyFLOperator(FLOperator op) {
		ArrayList<BasisState> newBasisStates = new ArrayList<BasisState>();
		for (BasisState state : basisStates) {
			if (state.whichQubit(state.getNumQubits() - 1) == 0 &&
					state.whichQubit(state.getNumQubits() - 2) == 0) {
				BasisState neighborState1 = state.clone();
				BasisState neighborState2 = state.clone();
				BasisState neighborState3 = state.clone();
				BasisState neighborState4 = state.clone();

				neighborState2.put(1, state.getNumQubits() - 1);
				neighborState3.put(1, state.getNumQubits() - 2);
				neighborState4.put(1, state.getNumQubits() - 2);
				neighborState4.put(1, state.getNumQubits() - 1);
				
				neighborState1.timesComplex(op.getElem(0, 0));
				neighborState2.timesComplex(op.getElem(0, 1));
				neighborState3.timesComplex(op.getElem(0, 2));
				neighborState4.timesComplex(op.getElem(0, 3));

				boolean add1 = true, add2 = true, add3 = true, add4 = true;

				for (BasisState b : newBasisStates) {
					if (b.equals(neighborState1)) {
						b.addAmplitude(neighborState1.getAmplitude());
						add1 = false;
					}
					if (b.equals(neighborState2)) {
						b.addAmplitude(neighborState2.getAmplitude());
						add2 = false;
					}
					if (b.equals(neighborState3)) {
						b.addAmplitude(neighborState3.getAmplitude());
						add3 = false;
					}
					if (b.equals(neighborState4)) {
						b.addAmplitude(neighborState4.getAmplitude());
						add4 = false;
					}

				}

				if (add1)
					newBasisStates.add(neighborState1);
				if (add2)
					newBasisStates.add(neighborState2);
				if (add3)
					newBasisStates.add(neighborState3);
				if (add4)
					newBasisStates.add(neighborState4);				
			}
			
			if (state.whichQubit(state.getNumQubits() - 1) == 1 &&
					state.whichQubit(state.getNumQubits() - 2) == 0) {
				BasisState neighborState1 = state.clone();
				BasisState neighborState2 = state.clone();
				BasisState neighborState3 = state.clone();
				BasisState neighborState4 = state.clone();

				neighborState1.put(0, state.getNumQubits() - 1);
				neighborState3.put(1, state.getNumQubits() - 2);
				neighborState4.put(1, state.getNumQubits() - 2);
				neighborState4.put(1, state.getNumQubits() - 1);
				
				neighborState1.timesComplex(op.getElem(1, 0));
				neighborState2.timesComplex(op.getElem(1, 1));
				neighborState3.timesComplex(op.getElem(1, 2));
				neighborState4.timesComplex(op.getElem(1, 3));

				boolean add1 = true, add2 = true, add3 = true, add4 = true;

				for (BasisState b : newBasisStates) {
					if (b.equals(neighborState1)) {
						b.addAmplitude(neighborState1.getAmplitude());
						add1 = false;
					}
					if (b.equals(neighborState2)) {
						b.addAmplitude(neighborState2.getAmplitude());
						add2 = false;
					}
					if (b.equals(neighborState3)) {
						b.addAmplitude(neighborState3.getAmplitude());
						add3 = false;
					}
					if (b.equals(neighborState4)) {
						b.addAmplitude(neighborState4.getAmplitude());
						add4 = false;
					}

				}

				if (add1)
					newBasisStates.add(neighborState1);
				if (add2)
					newBasisStates.add(neighborState2);
				if (add3)
					newBasisStates.add(neighborState3);
				if (add4)
					newBasisStates.add(neighborState4);
			}
			
			if (state.whichQubit(state.getNumQubits() - 1) == 0 &&
					state.whichQubit(state.getNumQubits() - 2) == 1) {
				BasisState neighborState1 = state.clone();
				BasisState neighborState2 = state.clone();
				BasisState neighborState3 = state.clone();
				BasisState neighborState4 = state.clone();

				neighborState1.put(0, state.getNumQubits() - 2);
				neighborState2.put(0, state.getNumQubits() - 2);
				neighborState2.put(1, state.getNumQubits() - 1);
				neighborState4.put(1, state.getNumQubits() - 1);
				
				neighborState1.timesComplex(op.getElem(2, 0));
				neighborState2.timesComplex(op.getElem(2, 1));
				neighborState3.timesComplex(op.getElem(2, 2));
				neighborState4.timesComplex(op.getElem(2, 3));

				boolean add1 = true, add2 = true, add3 = true, add4 = true;

				for (BasisState b : newBasisStates) {
					if (b.equals(neighborState1)) {
						b.addAmplitude(neighborState1.getAmplitude());
						add1 = false;
					}
					if (b.equals(neighborState2)) {
						b.addAmplitude(neighborState2.getAmplitude());
						add2 = false;
					}
					if (b.equals(neighborState3)) {
						b.addAmplitude(neighborState3.getAmplitude());
						add3 = false;
					}
					if (b.equals(neighborState4)) {
						b.addAmplitude(neighborState4.getAmplitude());
						add4 = false;
					}

				}

				if (add1)
					newBasisStates.add(neighborState1);
				if (add2)
					newBasisStates.add(neighborState2);
				if (add3)
					newBasisStates.add(neighborState3);
				if (add4)
					newBasisStates.add(neighborState4);
				
			}
			
			if (state.whichQubit(state.getNumQubits() - 1) == 1 &&
					state.whichQubit(state.getNumQubits() - 2) == 1) {
				BasisState neighborState1 = state.clone();
				BasisState neighborState2 = state.clone();
				BasisState neighborState3 = state.clone();
				BasisState neighborState4 = state.clone();

				neighborState1.put(0, state.getNumQubits() - 2);
				neighborState1.put(0, state.getNumQubits() - 1);
				neighborState2.put(0, state.getNumQubits() - 2);
				neighborState3.put(0, state.getNumQubits() - 1);
				
				neighborState1.timesComplex(op.getElem(3, 0));
				neighborState2.timesComplex(op.getElem(3, 1));
				neighborState3.timesComplex(op.getElem(3, 2));
				neighborState4.timesComplex(op.getElem(3, 3));

				boolean add1 = true, add2 = true, add3 = true, add4 = true;

				for (BasisState b : newBasisStates) {
					if (b.equals(neighborState1)) {
						b.addAmplitude(neighborState1.getAmplitude());
						add1 = false;
					}
					if (b.equals(neighborState2)) {
						b.addAmplitude(neighborState2.getAmplitude());
						add2 = false;
					}
					if (b.equals(neighborState3)) {
						b.addAmplitude(neighborState3.getAmplitude());
						add3 = false;
					}
					if (b.equals(neighborState4)) {
						b.addAmplitude(neighborState4.getAmplitude());
						add4 = false;
					}

				}

				if (add1)
					newBasisStates.add(neighborState1);
				if (add2)
					newBasisStates.add(neighborState2);
				if (add3)
					newBasisStates.add(neighborState3);
				if (add4)
					newBasisStates.add(neighborState4);
			}			
		}
		basisStates = newBasisStates;
		removeNullElements();
	}
	
	public void removeNullElements() {
		ArrayList<BasisState> newBasisStates = new ArrayList<BasisState>();
		for (BasisState b : basisStates) {
			if (b.getAmplitude().squaredMagnitude() > 0)
				newBasisStates.add(b);
		}
		basisStates = newBasisStates;
	}
	
	public void applyTLOperator(TLOperator op, int pos) {
		swap(pos, numQubits - 1, 1);
		applyTLOperator(op);
		swap(pos, numQubits - 1, 1);
	}
	
	public void applyFLOperator(FLOperator op, int pos1, int pos2) {
		swap(pos1, numQubits - 2, 1);
		swap(pos2, numQubits - 1, 1);
		applyFLOperator(op);
		swap(pos1, numQubits - 2, 1);
		swap(pos2, numQubits - 1, 1);

	}

}
