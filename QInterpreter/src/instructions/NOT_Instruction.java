package instructions;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;


public class NOT_Instruction implements Instruction {

	private String arg;
	private String result;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int r1 = mem.getRegValue(arg);
		mem.setRegValue(result, ~r1);
		f.setIndex(f.getIndex() + 1);						
	}

	@Override
	public String getName() {
		System.out.println("NOT " + arg + " " + result);
		return "NOT";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg = parsed[1];
		result = parsed[2];
	}
	
}
