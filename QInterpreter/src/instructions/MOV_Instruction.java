package instructions;
import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;


public class MOV_Instruction implements Instruction {

	private String src, dest;
	
	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		int r = mem.getRegValue(src);
		mem.setRegValue(dest, r);
		f.setIndex(f.getIndex() + 1);		
	}

	@Override
	public String getName() {
		System.out.println("MOV " + src + " " + dest);
		return "MOV";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		src = parsed[1];
		dest = parsed[2];
	}

}
