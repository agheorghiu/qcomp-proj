package instructions;

import java.util.Random;

import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class RAND_Instruction implements Instruction {

	private String arg;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		Random generator = new Random();
		Integer val = generator.nextInt(mem.getMaxMem());
		mem.store(mem.getRegisterAddress(arg) / 8, val.toString());
		f.setIndex(f.getIndex() + 1);
	}

	@Override
	public String getName() {
		return "RAND";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg = parsed[1];
	}
	

}
