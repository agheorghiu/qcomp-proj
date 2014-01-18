package instructions;

import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class STORE_Instruction implements Instruction {

	private String arg1, arg2;
	private String offset;
	

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int address = mem.getRegValue(arg2) + mem.getRegValue(offset);
		mem.store(address, arg1);
		f.setIndex(f.getIndex() + 1);
	}

	@Override
	public String getName() {
		System.out.println("STORE " + arg1 + " " + arg2 + " " + offset);
		return "STORE";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		int open = text.indexOf('[') + 1;
		int closed = (text.indexOf('+') < 0 ? text.indexOf(']') : text.indexOf('+'));
		arg2 = text.substring(open, closed).trim();
		if (text.indexOf('+') > 0) {
			open = text.indexOf('+') + 1;
			closed = text.indexOf(']');
			offset = text.substring(open, closed).trim();
		}
		else
			offset = "0";
	}
}
