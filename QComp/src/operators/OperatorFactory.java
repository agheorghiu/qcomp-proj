package operators;

import java.lang.reflect.Constructor;

import representation.IRegister;

public class OperatorFactory {
	
	private IRegister reg;
	
	public OperatorFactory(IRegister reg) {
		this.reg = reg;
	}
	
	/**
	 * 
	 * Really awesome method
	 * 
	 * 
	 * @param gateName
	 * @return
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
