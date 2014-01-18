package instructions;

import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class ALLOC_Instruction implements Instruction {
	private String arg1, arg2;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int numQubytes = mem.getRegValue(arg1);
		Integer addr = mem.allocate(numQubytes);
		mem.store(mem.getRegisterAddress(arg2) / 8, addr.toString());
		f.setIndex(f.getIndex() + 1);
	}

	@Override
	public String getName() {
		System.out.println("ALLOC " + arg1 + " " + arg2);
		return "ALLOC";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		arg2 = parsed[2];
	}
}
