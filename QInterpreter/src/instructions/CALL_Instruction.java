package instructions;
import java.util.Scanner;

import virtualMachine.Flow;
import virtualMachine.Memory;
import virtualMachine.VirtualMachine;

public class CALL_Instruction implements Instruction {
	private String arg;

	@Override
	public void execute(Flow f) throws Exception {
		Memory mem = VirtualMachine.getInstance().getMemory();
		if (arg.equals("__abort__")) {
			System.exit(1);
		}
		
		if (arg.equals("__inInt__")) {
			Scanner reader = new Scanner(System.in);
			int value = reader.nextInt();
			reader.close();
			mem.setRegValue("VR_RET", value);
			f.setIndex(f.getIndex() + 1);
			return;
		}
		
		if (arg.equals("__in_int__")) {
			Scanner reader = new Scanner(System.in);
			int value = reader.nextInt();
			reader.close();
			int addr = mem.getRegValue("Int_prototype"); // address of prototype
			int size = mem.getMemLocation(addr + 2, 2);
			int objAddr = mem.allocate(size * 2);
			for (int i = 0; i < size; i++) {
				int field = mem.getMemLocation(addr + i * 2, 2);
				mem.store(objAddr + i * 2, field + "");
			}
			mem.store(objAddr + 6, value + "");
			mem.setRegValue("VR_RET", objAddr + "");
			f.setIndex(f.getIndex() + 1);
			return;
		}
		
		if (arg.equals("__outInt__")) {
			mem.pop("VR_RET");
			int val = mem.getRegValue("VR_RET");
			System.out.println(val);
			f.setIndex(f.getIndex() + 1);
			return;
		}
		
		if (arg.equals("__out_int__")) {
			mem.pop("VR_RET");
			int addr = mem.getRegValue("VR_RET");
			int value = mem.getMemLocation(addr + 6, 2);
			System.out.println(value);
			f.setIndex(f.getIndex() + 1);
			return;
		}

		if (arg.equals("__in_string__")) {
			return;
		}
		
		if (arg.equals("__out_string__")) {
			mem.pop("VR_RET");
			int addr = mem.getRegValue("VR_RET");
			int lenAddr = mem.getMemLocation(addr + 6, 2);
			int lenValue = mem.getMemLocation(lenAddr + 6, 2);
			for (int i = 0; i < lenValue - 3; i++) {
				int ch = mem.getMemLocation(addr + 8 + i, 1);
				System.out.print((char)ch);
			}
			f.setIndex(f.getIndex() + 1);			
			return;
		}

		Integer currentAddress = f.getIndex() + 1;
		mem.push(currentAddress.toString());
		int r1 = mem.getRegValue(arg);
		f.setByAddress(r1);
	}

	@Override
	public String getName() {
		System.out.println("CALL " + arg);
		return "CALL";
	}

	@Override
	public void initialize(String text) {
		String[] parsed = text.split(" ");
		arg = parsed[1];
	}
}