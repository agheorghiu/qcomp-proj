package virtualMachine;
import instructions.Instruction;

import java.util.ArrayList;
import java.util.HashMap;

import exceptions.InvalidAddressException;

/**
 * 
 * Class for storing program flow.
 * 
 * @author Andru
 *
 */
public class Flow {
	private HashMap<Integer, Instruction> instructions;
	private HashMap<String, Integer> labels;
	private ArrayList<Integer> instrLines;
	private int index;
	
	public Flow() {
		instructions = new HashMap<Integer, Instruction>();
		labels = new HashMap<String, Integer>();
		instrLines = new ArrayList<Integer>();
		index = 0;
	}
	
	public void addInstruction(Instruction instr, int lineNo) {
		instructions.put(lineNo, instr);
		instrLines.add(lineNo);
	}
	
	public void addLabel(String label, int lineNo) {		
		labels.put(label, lineNo);
	}
	
	public Instruction getCurrentInstruction() {
		if (index >= instrLines.size())
			return null;
		int ip = instrLines.get(index);
		return instructions.get(ip);
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getAddress(int index) {
		return instrLines.get(index);
	}
	
	public int getAddress(String label) {
		if (labels.get(label) == null)
			return -1;
		return labels.get(label);
	}
	
	public void setIndex(int newIndex) {
		index = newIndex;
	}
	
	// Questionable
	public void setByAddress(int address) throws InvalidAddressException {
		int i;
		
		if (instrLines.size() == 0 || (instrLines.size() == 1 && address != 1))
			throw new InvalidAddressException();
		
		if (address > instrLines.get(instrLines.size() - 1)) {
			setIndex(instrLines.size());
			return;
		}
		
		for (i = 1; i < instrLines.size(); i++) {
			if (address < instrLines.get(i))
				break;
		}
		setIndex(i - 1);
	}

}
