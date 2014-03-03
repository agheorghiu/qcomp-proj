package representation;

/**
 * 
 * Class which defines a triplet (a grouping of 3 objects(
 * 
 * @author Andru, Charlie, Sam
 *
 * @param <A>	type of first object in triplet
 * @param <B>	type of second object in triplet
 * @param <C>	type of third object in triplet
 */
public class Triplet<A, B, C> {
	/**
	 * 
	 * First object in triplet
	 * 
	 */
	private A first;
	
	/**
	 * 
	 * Second object in triplet
	 * 	
	 */
	private B second;
	
	/**
	 * 
	 * Third object in triplet
	 * 
	 */
	private C third;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param first	first object
	 * @param second	second object
	 * @param third	third object
	 */
	public Triplet(A first, B second, C third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	/**
	 * 
	 * Returns first object in triplet
	 * 
	 * @return	first object
	 */
	public A fst() {
		return first;
	}
	
	/**
	 * 
	 * Returns second object in triplet
	 * 
	 * @return	second object
	 */
	public B snd() {
		return second;
	}
	
	/**
	 * 
	 * Returns third object in triplet
	 * 
	 * @return	third object
	 */
	public C trd() {
		return third;
	}
}
