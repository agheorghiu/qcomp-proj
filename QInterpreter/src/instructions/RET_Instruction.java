package instructions;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;


public class RET_Instruction implements Instruction {
	private String arg;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		if (arg != null) {
			mem.store(mem.getRegisterAddress("VR_RET") / 8, arg);
		}
		int addr = mem.pop();
		//System.out.println("JUMPING TO " + addr);
		f.setIndex(addr);	
	}

	@Override
	public String getName() {
		if (arg != null)
			System.out.println("RET " + arg);
		else
			System.out.println("RET");
		return "RET";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		if (parsed.length < 2 || parsed[1].startsWith("#"))
			arg = null;
		else
			arg = parsed[1];
	}
}
