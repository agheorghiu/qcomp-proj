package operators;

import java.lang.reflect.Constructor;

import representation.IRegister;

/**
 * 
 * Factory class for creating operators
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class OperatorFactory {
	
	/**
	 * 
	 * Register on which gates produced by the factory will operate
	 * 
	 */
	private IRegister reg;
	
	/**
	 * 
	 * Constructor for factory class
	 * 
	 * @param reg	register on which gates will operate
	 */
	public OperatorFactory(IRegister reg) {
		this.reg = reg;
	}
	
	/**
	 * 
	 * Really awesome method
	 * Method which uses reflection to obtain a specific gate (object) given the name
	 * of the gate
	 * 
	 * @param gateName	name of the gate we want
	 * @return	gate (operator) associated with gateName
	 */
	public Operator makeOperator(String gateName) {
		Class<?> classType;
		Operator object = null;
		try {
			classType = Class.forName("gates." + gateName);
			Constructor<?> constructor = classType.getConstructor(IRegister.class);
			object = (Operator)constructor.newInstance(reg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
}
