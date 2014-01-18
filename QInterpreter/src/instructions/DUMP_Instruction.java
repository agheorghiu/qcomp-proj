package instructions;

import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class DUMP_Instruction implements Instruction {
	
	private String arg;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		System.out.println();
		if (arg.equals("ALL")) {
			System.out.println("Register values: ");
			System.out.println("VR0 " + mem.getRegValue("VR0"));
			System.out.println("VR1 " + mem.getRegValue("VR1"));
			System.out.println("VR2 " + mem.getRegValue("VR2"));
			System.out.println("VR3 " + mem.getRegValue("VR3"));
			System.out.println("VR4 " + mem.getRegValue("VR4"));
			System.out.println("VR5 " + mem.getRegValue("VR5"));
			System.out.println("VR6 " + mem.getRegValue("VR6"));
			System.out.println("VR7 " + mem.getRegValue("VR7"));
			System.out.println("VR_RET " + mem.getRegValue("VR_RET"));
		}
		else {
			System.out.println("Register value: ");
			System.out.println(arg + " " + mem.getRegValue(arg));
		}
		f.setIndex(f.getIndex() + 1);
	}

	@Override
	public String getName() {
		return "DUMP";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg = parsed[1];
	}
}
