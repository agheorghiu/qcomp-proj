package virtualMachine;

import java.util.HashMap;

import exceptions.OutOfMemoryException;

import representation.FLOperator;
import representation.TLOperator;


/**
 * 
 * The virtual quantum memory
 * 
 * @author Andru
 *
 */
public class Memory {
	private final int registerSize = 16;
	private final int stackElementSize = 16;
	private final int numRegisters = 8;
	private final int totalMainMemory = (int)Math.pow(2, registerSize);
	private final int stackSize = 1024;
	private int stackPointer = 0;
	private final int mappedMemoryStart = stackSize * 8;
	private final int mappedMemorySize = 1024;
	private final int heapStart = mappedMemoryStart + mappedMemorySize * 8;
	private final int heapSize = totalMainMemory - stackSize - mappedMemorySize;
	private int[] registerAddresses;
	private GlobalMemoryState gms;
	private static Memory instance = null;
	private int freeMemStart = heapStart;
	private HashMap<String, Integer> labels;
	
	private Memory() {
		labels = new HashMap<String, Integer>();
		int memoryEnd = totalMainMemory * 8;
		gms = new GlobalMemoryState(totalMainMemory * 8 + numRegisters * registerSize);
		registerAddresses = new int[numRegisters];
		for (int i = 0; i < numRegisters; i++)
			registerAddresses[i] = memoryEnd + i * 16;
	}
	
	public static Memory getInstance() {
		if (instance == null)
			instance = new Memory();
		return instance;
	}
	
	public void addLabel(String label, int address) {
		labels.put(label, address);
	}
	
	public int getLabelAddress(String label) {
		return labels.get(label);
	}
	
	public boolean hasRegister(String name) {
		if (name.equals("VR_RET"))
			return true;
		if (name.length() != 3)
			return false;
		String prefix = name.substring(0, 2);
		if (prefix.equals("VR")) {
			String index = name.substring(2, 3);
			return (Integer.parseInt(index) < numRegisters - 1);				
		}
		return false;
	}
	
	public int getRegisterIndex(String regName) {
		if (regName.equals("VR_RET"))
			return (numRegisters - 1);
		String index = regName.substring(2, 3);
		return Integer.parseInt(index);
	}
	
	public int getRegisterAddress(String regName) {
		return registerAddresses[getRegisterIndex(regName)];
	}
	
	private int[] toBinary(int value, int size) {
		int[] vector = new int[size];
		int dup = value;
		for (int i = 0; i < size; i++) {
			if (dup % 2 == 0)
				vector[i] = 0;
			else
				vector[i] = 1;
			dup /= 2;
		}
		return vector;
	}
	
	private int toDecimal(int[] vector) {
		int dec = 0;
		for (int i = 0; i < vector.length; i++)
			dec += vector[i] * Math.pow(2, i);
		return dec;
	}

	public void push(String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			gms.loadstore(regAddr, stackPointer, stackElementSize);
			stackPointer += stackElementSize;
		}
		else {
			try {
				int value = Integer.parseInt(register);
				int vector[] = toBinary(value, stackElementSize);
				gms.loadstore(vector, stackPointer, stackElementSize);
				stackPointer += stackElementSize;
			} catch (Exception e) {
				int value = VirtualMachine.getInstance().getFlow().getAddress(register);
				if (value == -1)
					value = getLabelAddress(register);
				int vector[] = toBinary(value, stackElementSize);
				gms.loadstore(vector, stackPointer, stackElementSize);
				stackPointer += stackElementSize;
			}
		}
	}
	
	public void pop(String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			stackPointer -= stackElementSize;
			gms.loadstore(stackPointer, regAddr, stackElementSize);
		}
	}
	
	public int pop() {
		stackPointer -= stackElementSize;
		int[] vector = gms.getCollapsedMemLocation(stackPointer, stackElementSize);
		return toDecimal(vector);
	}
	
	public void put(int index, String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			gms.loadstore(regAddr, mappedMemoryStart + index * registerSize, registerSize);
		}
		else {
			try {
				int value = Integer.parseInt(register);
				int vector[] = toBinary(value, registerSize);
				gms.loadstore(vector, mappedMemoryStart + index * registerSize, registerSize);
			} catch (Exception e) {
				int value = VirtualMachine.getInstance().getFlow().getAddress(register);
				if (value == -1)
					value = getLabelAddress(register);
				int vector[] = toBinary(value, registerSize);
				gms.loadstore(vector, mappedMemoryStart + index * registerSize, registerSize);
			}
		}
	}
	
	public void get(int index, String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			gms.loadstore(mappedMemoryStart + index * registerSize, regAddr, registerSize);
		}
	}
	
	public int getMappedMemorySize() {
		return mappedMemorySize;
	}
	
	public void load(int address, String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			gms.loadstore(address * 8, regAddr, registerSize);
		}	
	}
	
	public void store(int address, String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			gms.loadstore(regAddr, address * 8, registerSize);
		}
		else {
			try {
				int value = Integer.parseInt(register);
				int vector[] = toBinary(value, registerSize);
				gms.loadstore(vector, address * 8, registerSize);
			} catch (Exception e) {
				int value = VirtualMachine.getInstance().getFlow().getAddress(register);
				if (value == -1)
					value = getLabelAddress(register);
				int vector[] = toBinary(value, registerSize);
				gms.loadstore(vector, address * 8, registerSize);
			}
		}
	}

	public void loadb(int address, String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			gms.loadstore(address * 8, regAddr, registerSize / 2);
		}		
	}

	public void storeb(int address, String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			gms.loadstore(regAddr, address * 8, registerSize / 2);
		}
		else {
			try {
				int value = Integer.parseInt(register);
				int vector[] = toBinary(value, registerSize / 2);
				gms.loadstore(vector, address * 8, registerSize / 2);
			} catch (Exception e) {
				int value = VirtualMachine.getInstance().getFlow().getAddress(register);
				if (value == -1)
					value = getLabelAddress(register);
				int vector[] = toBinary(value, registerSize / 2);
				gms.loadstore(vector, address * 8, registerSize / 2);
			}
		}		
	}
	
	public void swap(String register1, String register2) {
		if (hasRegister(register1) && hasRegister(register2)) {
			int regAddr1 = registerAddresses[getRegisterIndex(register1)];
			int regAddr2 = registerAddresses[getRegisterIndex(register2)];
			gms.swap(regAddr1, regAddr2, registerSize);
		}
	}
	
	public void swapb(String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			gms.swap(regAddr, regAddr + registerSize / 2, registerSize / 2);
		}
	}	
	
	public void reverse(String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			gms.reverse(regAddr, registerSize);
		}
	}	

	public int getRegValue(String register) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			return toDecimal(gms.getCollapsedMemLocation(regAddr, registerSize));
		}
		else {
			try {
				int value = Integer.parseInt(register);
				return value;
			} catch (Exception e) {
				int value = VirtualMachine.getInstance().getFlow().getAddress(register);
				if (value == -1)
					value = getLabelAddress(register);
				return value;
			}
		}
	}
	
	public void setRegValue(String register, int value) {
		int[] vector = toBinary(value, registerSize);
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			gms.loadstore(vector, regAddr, registerSize);
		}
	}
	
	public void setRegValue(String register, String label) {
		if (hasRegister(register)) {
			int regAddr = registerAddresses[getRegisterIndex(register)];
			int value = getRegValue(label);
			int[] vector = toBinary(value, registerSize);			
			gms.loadstore(vector, regAddr, registerSize);
		}
	}
	
	public void measure(String targetReg, String mask) {
		int[] maskVal = toBinary(getRegValue(mask), registerSize);
		int base = getRegisterAddress(targetReg);
		int numMeasure = 0;
		for (int i = 0; i < maskVal.length; i++)
			if (maskVal[i] != 0)
				numMeasure++;
		int[] indexes = new int[numMeasure];
		numMeasure = 0;
		for (int i = 0; i < maskVal.length; i++)
			if (maskVal[i] != 0) {
				indexes[numMeasure] = base + i;
				numMeasure++;
			}
		gms.measure(indexes);
	}
	
	public void applyTLOperator(TLOperator op, String targetReg, String mask) {
		int[] binMask = toBinary(getRegValue(mask), registerSize);
		int base = getRegisterAddress(targetReg);
		for (int i = 0; i < binMask.length; i++) {
			if (binMask[i] == 1)
				gms.applyTLOperator(op, base + i);
		}
	}
	
	
	public void applyFLOperator(FLOperator op, String targetReg, String control) {
		int targetAddr = getRegisterAddress(targetReg);
		int ctrlAddr = getRegisterAddress(control);
		
		for (int i = 0; i < registerSize; i++) {
			gms.applyFLOperator(op, ctrlAddr + i, targetAddr + i);
		}
	}
	
	public void applyFLOperatorSingle(FLOperator op, String targetReg) {
		int targetAddr = getRegisterAddress(targetReg);
		
		for (int i = 0; i < registerSize; i++) {
			gms.applyFLOperator(op, targetAddr + (registerSize / 2) + i, targetAddr + i);
		}
	}
	
	public void showMemLocation(int addr, int size) {
		gms.showMemLocation(addr, size);
	}
	
	public int getMemLocation(int addr, int size) {
		return toDecimal(gms.getCollapsedMemLocation(addr * 8, size * 8));
	}
	
	public int allocate(int numQubytes) {
		int address = freeMemStart;
		freeMemStart += 8 * numQubytes;
		if (freeMemStart - heapStart > heapSize) {
			System.err.println("OUT OF MEMORY");
			System.exit(1);
		}
		return address / 8;
	}
	
	public int getFreeMemStart() {
		return freeMemStart / 8;
	}

	public int getMaxMem() {
		return totalMainMemory;
	}
}
