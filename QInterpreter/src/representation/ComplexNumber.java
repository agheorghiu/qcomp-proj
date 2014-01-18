package representation;

public class ComplexNumber {
	private double re, im;
	
	public ComplexNumber(double re, double im) {
		this.re = re;
		this.im = im;
	}
	
	public double getRe() {
		return re;
	}
	
	public double getIm() {
		return im;
	}
	
	public void add(ComplexNumber c) {
		this.re += c.re;
		this.im += c.im;
	}
	
	public void mul(ComplexNumber c) {
		double re = this.re;
		double im = this.im;
		this.re = re * c.re - im * c.im;
		this.im = re * c.im + im * c.re;
	}
	
	public double squaredMagnitude() {
		return (re * re + im * im);
	}
	
	public void timesConstant(double value) {
		this.re = this.re * value;
		this.im = this.im * value;
	}

	public String toString() {
		String str = "";
		
		if (im == 0)
			str += re;
		else
			if (re == 0)
				str += "i * " + im; 
			else
				str += re + " + i * " + im;
		return str;
	}
}
