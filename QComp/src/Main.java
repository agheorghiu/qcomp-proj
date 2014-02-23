import algorithms.ArraySearch;


/**
 * 
 * Main class
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class Main {
	
	public static void main(String[] args) {
		int[] v = { 10, 14, 35, 18, 22, 5, 2, 17, 54, 123, 200, 33, 9 };
		ArraySearch search = new ArraySearch(v, 17);
		search.run();
	}
}