package instructions;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class PUT_Instruction implements Instruction {

	private String arg1, arg2;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int val = mem.getRegValue(arg1);
		mem.put(val, arg2);
		f.setIndex(f.getIndex() + 1);	
	}

	@Override
	public String getName() {
		System.out.println("PUT " + arg1 + " " + arg2);
		return "PUT";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		arg2 = parsed[2];
	}
}
