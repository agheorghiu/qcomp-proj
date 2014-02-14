import algorithms.Algorithm;
import algorithms.DeutschJozsa;


/**
 * 
 * Main class
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class Main {
	
	public static void main(String[] args) {
		Algorithm deutschJozsa = new DeutschJozsa(5);
		deutschJozsa.run();
	}
}