package representation;

public class BinaryArray {
	private boolean[] array;
	
	public BinaryArray(int n) {
		array = new boolean[n];
	}
	
	public BinaryArray(boolean[] array) {
		this.array = array;
	}
	
	public void flipIndex(int index) {
		array[index] = !array[index];
	}
	
	public int hashCode() {
		int code = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i])
				code += (int)Math.pow(2, i);
		}
		return code;
	}
	
	public boolean equals(Object o) {
		try {
			BinaryArray  b = (BinaryArray)o;
			if (array.length != b.array.length)
				return false;
			return (this.hashCode() == b.hashCode());
		} catch (Exception e) {
			return false;
		}
	}
	
	public String toString() {
		String s = "";
		for (int i = 0; i < array.length; i++)
			if (array[i])
				s += "1";
			else
				s += "0";
		return s;
	}
}
