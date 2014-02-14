package representation;

/**
 * 
 * Tuple class for storing pairs of objects
 * 
 * @author Andru, Charlie, Sam
 *
 * @param <U>	type of first parameter
 * @param <V>	type of second parameter
 */
public class Tuple<U,V> {
	
	/**
	 *
	 * First parameter
	 * 
	 */
	private U first;
	
	/**
	 * 
	 * Second parameter
	 * 
	 */
	private V second;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param first	first parameter
	 * @param second	second parameter
	 */
	public Tuple(U first, V second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * 
	 * Getter function for first parameter
	 * 
	 * @return	returns first parameter
	 */
	public U fst() {
		return first;
	}
	
	/**
	 * 
	 * Getter function for second parameter
	 * 
	 * @return	returns second parameter
	 */
	public V snd() {
		return second;
	}
}
