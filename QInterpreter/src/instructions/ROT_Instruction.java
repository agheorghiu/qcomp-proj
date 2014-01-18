package instructions;

import representation.ComplexNumber;
import representation.TLOperator;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class ROT_Instruction implements Instruction {
	
	private String arg1, arg2, arg3;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int angle = mem.getRegValue(arg3);
		double angleRad = Math.PI * (double)angle / 180.0;
		double cos = Math.cos(angleRad);
		double sin = Math.sin(angleRad);
		System.out.println("+++++" + cos);
		System.out.println("+++++" + sin);
		TLOperator op = new TLOperator(new ComplexNumber(1, 0), 
				new ComplexNumber(0, 0), new ComplexNumber(0, 0), new ComplexNumber(cos, sin));		
		mem.applyTLOperator(op, arg1, arg2);
		f.setIndex(f.getIndex() + 1);
	}

	@Override
	public String getName() {
		return "ROT";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		arg2 = parsed[2];
		arg3 = parsed[3];
	}
}
