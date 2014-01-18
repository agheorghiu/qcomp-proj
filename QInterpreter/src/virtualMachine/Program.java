package virtualMachine;
import instructions.Instruction;
import instructions.InstructionTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * Class which defines a program in QOOL IR. Allows reading and executing the program.
 * 
 * @author Andru
 *
 */
public class Program {
	
	private Flow flow;
	private Memory virtualMemory;
	
	public Program(String fileName, Flow flow, Memory virtualMemory) {
		try {
			Scanner reader = new Scanner(new File(fileName));
			String line;
			this.flow = flow;
			this.virtualMemory = virtualMemory;
			if (!reader.hasNextLine())
				return;
			
			while (reader.hasNext()) {
				line = reader.nextLine();
				line = line.trim();
				line = line.replaceAll("\\s+", " ");
				if (line.length() > 0 && line.charAt(0) != '#' && line.substring(0, 5).equals(".code"))
					break;
			}

			readCode(reader);
			readData(reader);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void readCode(Scanner reader) {
		String line;
		int index = 0;
		while (reader.hasNextLine()) {
			line = reader.nextLine();
			line = line.trim();
			line = line.replaceAll("\\s+", " ");
			
			if (line.equals(".data"))
				break;
			if (line.length() > 0 && line.charAt(0) != '#' && line.charAt(0) != '.') {
				index++;
				processCodeLine(line, index);
			}
		}	
	}

	private void readData(Scanner reader) {
		String line;
		ArrayList<Directive> directives = new ArrayList<Directive>();
		while (reader.hasNextLine()) {
			line = reader.nextLine();
			//System.out.println("Uite linia" + line);
			if (line.indexOf("#") > 0)
				line = line.substring(0, line.indexOf("#"));
			line = line.trim();
			line = line.replaceAll("\\s+", " ");
			if (line.length() > 0 && line.charAt(0) != '#' && line.charAt(0) != '.') {
				processDataLine(line, directives);
			}
		}
		processData(directives);
	}
	
	private void processData(ArrayList<Directive> directives) {
		for (Directive d : directives) {
			if (d.getType().equals("DW")) {
				virtualMemory.store(d.getAddress(), d.getContent());
			}
			if (d.getType().equals("DL")) {
				int value = flow.getAddress(d.getContent());
				if (value == -1)
					value = virtualMemory.getLabelAddress(d.getContent());
				d.setValue(value);
				virtualMemory.store(d.getAddress(), value + "");
			}
			if (d.getType().equals("DB")) {
				if (d.getValue() < 0) {
					int offset = 0;
					for (Character c : d.getContent().toCharArray()) {
						virtualMemory.storeb(d.getAddress() + offset, (int)c + "");
						offset++;
					}
				}
				else {
					virtualMemory.storeb(d.getAddress(), d.getContent());
				}
			}
		}		
	}
	
	private void processCodeLine(String line, int lineNo) {
		if (line.equals("\n"))
			return;
		if (isInstruction(line)) {
			Instruction instr = getInstruction(line);
			flow.addInstruction(instr, lineNo);
		}
		else {
			if (isLabel(line)) {
				String label = getLabel(line);
				flow.addLabel(label, lineNo + 1);
			}
		}
	}
	
	private void processDataLine(String line, ArrayList<Directive> directives) {
		if (isLabel(line)) {
			//System.out.println(line + "xxx" + virtualMemory.getFreeMemStart());
			virtualMemory.addLabel(getLabel(line), virtualMemory.getFreeMemStart());
		}
		else {
			String newline = line.replaceAll("\"", "");
			String dirName = newline.substring(0, 2);
			String content = newline.substring(3, newline.length());
			Directive d = new Directive(dirName, content, virtualMemory.getFreeMemStart());
			virtualMemory.allocate(d.getSize());
			directives.add(d);
		}
	}
	
	public void execute() throws Exception {
		Instruction current = flow.getCurrentInstruction();
		while (current != null) {
			current.execute(flow);
			current = flow.getCurrentInstruction();
		}
	}
	
	private Instruction getInstruction(String instrText) {
		String instrName = "instructions." + instrText.split(" ")[0].toUpperCase();
		instrName += "_Instruction";
		try {
			Class<?> instrClass = Class.forName(instrName);
			Instruction instr = (Instruction)instrClass.newInstance();
			instr.initialize(instrText);
			return instr;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getLabel(String line) {
		return line.substring(0, line.indexOf(':'));
	}
	
	private boolean isSegment(String line) {
		return ((line.length() > 0) && line.charAt(0) == '.');
	}
	
	private boolean isComment(String line) {
		return ((line.length() > 0) && line.charAt(0) == '#'); 
	}
	
	private boolean isInstruction(String line) {
		if (isComment(line))
			return false;
		String instr = line.split(" ")[0].toUpperCase();
		return InstructionTable.instrExists(instr);
	}
	
	private boolean isLabel(String line) {
		return (!isComment(line) && !isInstruction(line) && !isSegment(line));
	}
		
}
