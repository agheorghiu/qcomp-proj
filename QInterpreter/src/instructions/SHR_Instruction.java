package instructions;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;


public class SHR_Instruction implements Instruction {
	
	private String arg;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int r1 = mem.getRegValue(arg);
		mem.setRegValue(arg, r1 * 2);
		f.setIndex(f.getIndex() + 1);						
	}

	@Override
	public String getName() {
		return "SHR";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg = parsed[1];
	}
}
