package representation;

public class FLOperator {
	private ComplexNumber[][] values;
	
	public FLOperator(ComplexNumber[][] values) {
		this.values = values;
	}
	
	public ComplexNumber getElem(int line, int col) {
		return values[line][col];
	}
}
