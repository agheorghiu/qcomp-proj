package instructions;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;


public class JUMPT_Instruction implements Instruction {
	
	private String arg1, arg2;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int r1 = mem.getRegValue(arg1);
		int r2 = mem.getRegValue(arg2);
		if (r2 != 0)
			f.setByAddress(r1);	
		else
			f.setIndex(f.getIndex() + 1);
	}

	@Override
	public String getName() {
		System.out.println("JUMPT " + arg1 + " " + arg2);
		return "JUMPT";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		arg2 = parsed[2];
	}
}
