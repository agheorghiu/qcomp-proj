package algorithms;

/**
 * 
 * Implementation of an array search algorithm using Grover's algorithm
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class ArraySearch extends Grover implements Algorithm {

	/**
	 * 
	 * Array in which we are searching a value
	 * 
	 */
	private int[] array;
	
	/**
	 * 
	 * Value which we are searching in an array
	 * 
	 */
	private int element;
	
	/**
	 * 
	 * Maximum number of runs for the algorithm on failure
	 * 
	 */
	private final int maxRuns = 10;
	
	/**
	 * 
	 * "Default" constructor which receives number of qubits and the omega state
	 * In this case, this constructor is useless, since this is not what we plan to use
	 * for searching in an array.
	 * 
	 * @param numQubits	number of qubits
	 * @param omega	dummy omega
	 */
	private ArraySearch(int numQubits, int omega) {
		super(numQubits, omega);
	}
	
	/**
	 * 
	 * Actual constructor which receives the array in which we're searching for
	 * and the element we are searching for
	 * 
	 * @param array
	 * @param element
	 */
	public ArraySearch(int[] array, int element) {
		super((int)(Math.log(array.length) / Math.log(2) + 1), element);
		this.array = array;
		this.element = element;
	}
	
	@Override
	public void run() {
		int run = 0, computedIndex = -1;
		while ((computedIndex < 0 || !test(computedIndex)) && run < maxRuns) {
			computedIndex = searchState();
			run++;
			if (test(2 * computedIndex)) {
				System.out.println("Element found at position " + computedIndex);
				return;
			}
		}
		System.out.println("Element not found!");
	}

	/**
	 * 
	 * The oracle operator works exactly like in Grover's algorithm, except now each state corresponds to an
	 * index in the array in which we're searching for element. If the current index/state corresponds to the
	 * position where the element is located, then we return true, otherwise false. This is function f used
	 * in the black box.
	 * 
	 */
	protected boolean test(int state) {
		return ((state >> 1) < array.length && array[state >> 1] == element);
	}
}
