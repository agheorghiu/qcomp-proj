/**
 * 
 * Complex number implementation
 * 
 * @author Andru, Charlie, Sam
 *
 */
public class Complex {

	/**
	 * 
	 * Real and imaginary part
	 * 
	 */
	private double re, im;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param re	real part
	 * @param im	imaginary part
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	
	/**
	 * 
	 * Method for adding 2 complex numbers (modifies this)
	 * 
	 * @param c	complex number which is added to this
	 */
	public void add(Complex c) {
		this.re += c.re;
		this.im += c.im;
	}

	/**
	 * 
	 * Method for multiplying 2 complex numbers (modifies this)
	 * 
	 * @param c	complex number which is multiplied to this
	 */
	public void mul(Complex c) {
		double reAux, imAux;
		reAux = this.re * c.re - this.im * c.im;
		imAux = this.im * c.re + this.re * c.im;
		this.re = reAux;
		this.im = imAux;
	}
	
	/**
	 * 
	 * Method for computing modulus squared
	 * 
	 * @return	returns modulus squared of this complex number
	 */
	public double modS() {
		return (this.re * this.re + this.im * this.im);
	}
	
	/**
	 * 
	 * Method for computing modulus of this complex number
	 * 
	 * @return	returns modulus of this complex number
	 */
	public double mod() {
		return Math.sqrt(modS());
	}
	
	/**
	 * 
	 * Prints string representation
	 * 
	 */
	public String toString() {
		String str = "";
		str += re + " " + im + "i \n";
		return str;
	}

	/**
	 * 
	 * Creates the complex number 1
	 * 
	 * @return returns the complex number 1
	 */
	public static Complex one() {
		return new Complex(1, 0);
	}
	
	/**
	 * 
	 * Creates the complex number 0
	 * 
	 * @return returns the complex number 0
	 */
	public static Complex zero() {
		return new Complex(0, 0);
	}

}
