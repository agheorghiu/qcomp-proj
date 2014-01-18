package instructions;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;
import representation.ComplexNumber;
import representation.TLOperator;


public class H_Instruction implements Instruction {
	
	private String arg1, arg2;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		ComplexNumber ct = new ComplexNumber(1 / Math.sqrt(2), 0);
		ComplexNumber negct = new ComplexNumber(-1 / Math.sqrt(2), 0);
		TLOperator op = new TLOperator(ct, ct, ct, negct);
		mem.applyTLOperator(op, arg1, arg2);
		f.setIndex(f.getIndex() + 1);
	}

	@Override
	public String getName() {
		System.out.println("H " + arg1 + " " + arg2);
		return "H";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		arg2 = parsed[2];
	}
}
