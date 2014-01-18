package instructions;
import virtualMachine.Flow;

/**
 * 
 * Interface which defines a basic instruction/operation.
 * 
 * @author Andru
 *
 */
public interface Instruction {
	
	public void initialize(String text);
	
	public void execute(Flow f) throws Exception;
	
	public String getName();
	

}
