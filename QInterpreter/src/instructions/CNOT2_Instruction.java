package instructions;

import representation.ComplexNumber;
import representation.FLOperator;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class CNOT2_Instruction implements Instruction {
	
	private String arg1, arg2;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		ComplexNumber[][] matrix = new ComplexNumber[4][4];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				matrix[i][j] = new ComplexNumber(0, 0);
		matrix[0][0] = new ComplexNumber(1, 0);
		matrix[1][1] = new ComplexNumber(1, 0);
		matrix[2][3] = new ComplexNumber(1, 0);
		matrix[3][2] = new ComplexNumber(1, 0);
		FLOperator op = new FLOperator(matrix);
		mem.applyFLOperator(op, arg1, arg2);
		f.setIndex(f.getIndex() + 1);
	}

	@Override
	public String getName() {
		System.out.println("CNOT2 " + arg1 + " " + arg2);
		return "CNOT2";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg1 = parsed[1];
		arg2 = parsed[2];
	}
}
