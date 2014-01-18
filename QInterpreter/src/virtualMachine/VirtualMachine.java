package virtualMachine;
import java.util.HashMap;

/**
 * 
 * Class representing the virtual machine. Contains registers and memory
 * 
 * @author Andru
 *
 */
public class VirtualMachine {
	private static VirtualMachine vmInstance = null;
	private static Memory virtualMemory;
	private static Flow flow;
	private static String name;
	
	private VirtualMachine(String programName) {
		name = programName;
		virtualMemory = Memory.getInstance();
		flow = new Flow();
	}

	public static VirtualMachine getInstance() {
		return vmInstance;
	}
	
	public static VirtualMachine getFirstInstance(String name) {
		vmInstance = new VirtualMachine(name);
		return vmInstance;
	}
		
	public Memory getMemory() {
		return virtualMemory;
	}
	
	public Flow getFlow() {
		return flow;
	}
	
	public void run() throws Exception {
		Program p = new Program(name, flow, virtualMemory);
		p.execute();
	}
}
