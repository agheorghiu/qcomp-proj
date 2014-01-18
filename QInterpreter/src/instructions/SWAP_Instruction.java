package instructions;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;


public class SWAP_Instruction implements Instruction {
	
	private String arg1, arg2;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		mem.swap(arg1, arg2);
		f.setIndex(f.getIndex() + 1);			
	}

	@Override
	public String getName() {
		return "SWAP";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		arg2 = parsed[2];
	}
}
