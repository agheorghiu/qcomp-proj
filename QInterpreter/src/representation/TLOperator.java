package representation;

public class TLOperator {
	private ComplexNumber a, b, c, d;
	
	public TLOperator(ComplexNumber a, ComplexNumber b, ComplexNumber c, ComplexNumber d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public ComplexNumber getA() {
		return a;
	}
	
	public ComplexNumber getB() {
		return b;
	}

	public ComplexNumber getC() {
		return c;
	}

	public ComplexNumber getD() {
		return d;
	}

}
