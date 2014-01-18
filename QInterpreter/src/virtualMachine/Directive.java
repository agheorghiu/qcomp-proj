package virtualMachine;

public class Directive {
	private String type, content;
	private int size, address, value = -1;
	
	public Directive(String type, String content, int address) {
		this.address = address;
		if (type.equals("DB")) {
			this.type = type;
			this.content = content;
			this.size = 1;
			try {
				this.value = Integer.parseInt(content);
			} catch (Exception e) {
				this.value = -2;
				this.size = content.length();
			}
		}
		
		if (type.equals("DW")) {
			this.type = type;
			this.content = content;
			this.size = 2;
			this.value = Integer.parseInt(content);
		}
		
		if (type.equals("DL")) {
			this.type = type;
			this.content = content;
			this.size = 2;
		}
		
		if (type.equals("DS")) {
			this.type = type;
			this.content = content;
			this.size = Integer.parseInt(content);
			this.value = 0;
		}
	}
	
	public String getType() {
		return type;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getAddress() {
		return address;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getContent() {
		return content;
	}
	
	public String toString() {
		return type + " " + size + " " + content;
	}

}
