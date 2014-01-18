package instructions;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;


public class JUMP_Instruction implements Instruction {
	
	private String arg;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int r1 = mem.getRegValue(arg);
		f.setByAddress(r1);		
	}

	@Override
	public String getName() {
		System.out.println("JUMP " + arg);
		return "JUMP";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg = parsed[1];
	}
}
