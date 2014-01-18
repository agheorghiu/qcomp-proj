package instructions;

import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class GET_Instruction implements Instruction {

	private String arg1, arg2;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int val = mem.getRegValue(arg2);
		mem.get(val, arg1);
		f.setIndex(f.getIndex() + 1);		
	}

	@Override
	public String getName() {
		System.out.println("GET " + arg1 + " " + arg2);
		return "GET";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		arg2 = parsed[2];
	}

}
