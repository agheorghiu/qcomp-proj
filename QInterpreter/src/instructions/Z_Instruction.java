package instructions;

import representation.ComplexNumber;
import representation.TLOperator;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class Z_Instruction implements Instruction {
	private String arg1, arg2;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		TLOperator op = new TLOperator(new ComplexNumber(1, 0), 
				new ComplexNumber(0, 0), new ComplexNumber(0, 0), new ComplexNumber(-1,0));
		mem.applyTLOperator(op, arg1, arg2);
		f.setIndex(f.getIndex() + 1);
	}

	@Override
	public String getName() {
		return "Z";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		arg2 = parsed[2];
	}
}
