package virtualMachine;
import exceptions.OverflowException;


public class ConstantRegister extends Register {

	public ConstantRegister(int value) {
		super("Constant_"+value);
		try {
			super.fromDecimal(value);
			setContent(super.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ConstantRegister(String label) {
		super("Constant_" + label);
		try {
			int address = VirtualMachine.getInstance().getFlow().getAddress(label);
			super.fromDecimal(address);
			setContent(super.getContent());
		} catch (OverflowException e) {
			e.printStackTrace();
		}
	}	
}
