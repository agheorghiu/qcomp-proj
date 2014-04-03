package representation;
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
	 * Creates the complex number 1
	 * 
	 * @return returns the complex number 1
	 */
	public static Complex one() {
		return new Complex(1, 0);
	}
	
	/**
	 * 
	 * Creates the complex number i
	 * 
	 * @return returns the complex number i
	 */
	public static Complex i() {
		return new Complex(0, 1);
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
	 * Scale this complex number with a real factor
	 * 
	 * @param factor	real factor for scaling
	 */
	public void scale(double factor) {
		this.re = this.re * factor;
		this.im = this.im * factor;
	}
	
	/**
	 * 
	 * Add 2 complex numbers and return the result as a new complex number
	 * 
	 * @param c1	first complex number
	 * @param c2	second complex number
	 * @return	sum of the two complex numbers
	 */
	public static Complex sum(Complex c1, Complex c2) {
		Complex result = Complex.zero();
		result.add(c1);
		result.add(c2);
		return result;		
	}
	
	/**
	 * 
	 * Multiply 2 complex numbers and return the result as a new complex number
	 * 
	 * @param c1	first complex number
	 * @param c2	second complex number
	 * @return	result of multiplying the 2 complex numbers
	 */
	public static Complex multiply(Complex c1, Complex c2) {
		Complex result = Complex.one();
		result.mul(c1);
		result.mul(c2);
		return result;
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
	 * Method for negating a complex number
	 * 
	 * @return	returns a negated complex number
	 */
	public Complex negated() {
		return new Complex(-this.re, -this.im);
	}

	@Override
	public Complex clone(){
		return new Complex(this.re, this.im);
	}	
	
	@Override
	public String toString() {
		String str = "";
		str += re + " " + im + "i \n";
		return str;
	}
	
	@Override
	public boolean equals(Object o) {
		try {
			Complex c = (Complex)o;
			return equals(c);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * Tests for equality with other complex number
	 * 
	 * @param c	complex number to compare with
	 * @return	true or false depending on whether this complex number is equal to c
	 */
	public boolean equals(Complex c){
		if (this.re == c.re && this.im == c.im)
			return true;
		return false;
	}
		
}
