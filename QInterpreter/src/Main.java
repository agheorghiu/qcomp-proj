import virtualMachine.VirtualMachine;

public class Main {

	public static void main(String[] args) throws Exception {
		VirtualMachine vmMachine = VirtualMachine.getFirstInstance(args[0]);
		vmMachine.run();
	}
}