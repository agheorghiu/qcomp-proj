package instructions;

import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class NEW_Instruction implements Instruction {

	private String arg1, arg2;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int addr = mem.getRegValue(arg1); // address of prototype
		mem.setRegValue(arg2, mem.getFreeMemStart() + ""); // address of new object
		int size = mem.getMemLocation(addr + 2, 2);
		int objAddr = mem.allocate(size * 2);
		for (int i = 0; i < size; i++) {
			int field = mem.getMemLocation(addr + i * 2, 2);
			mem.store(objAddr + i * 2, field + "");
		}
		f.setIndex(f.getIndex() + 1);
	}

	@Override
	public String getName() {
		System.out.println("NEW " + arg1 + " " + arg2);
		return "NEW";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		arg2 = parsed[2];
	}

}
