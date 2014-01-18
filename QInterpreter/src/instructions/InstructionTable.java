package instructions;
/**
 *
 * Table for storing known instructions
 * 
 * @author Andru
 *
 */
public class InstructionTable {
	
	private static String[] instructions = { "NOT", // OK
											 "AND", // OK
											 "OR", // OK
											 "XOR", // OK
											 "ADD", // OK
											 "SUB", // OK
											 "MUL", // OK
											 "DIV", // OK
											 "GT", // OK
											 "LT", // OK
											 "EQ", // OK
											 "LOAD", // OK
											 "LOADB", // OK
											 "STORE", // OK
											 "STOREB", // OK
											 "NEW", // OK
											 "ALLOC", // OK
											 "PUT", // OK
											 "GET", // OK
											 "JUMP", // OK
											 "JUMPF", // OK
											 "JUMPT", // OK
											 "CALL", // OK
											 "RET", // OK
											 "PUSH", // OK
											 "POP", // OK
											 "MOV", // OK
											 "SWAP", // OK
											 "SWAPB", // OK
											 "SHL", // OK
											 "SHR", // OK
											 "CNOT", // OK
											 "CNOT2", // OK
											 "X", // OK
											 "Y", // OK
											 "Z", // OK
											 "T", // OK
											 "S", // OK
											 "H", // OK
											 "ROT", // OK
											 "REV", // OK
											 "MEAS", // OK
											 "RAND", // OK
											 "QMOV", // OK
											 "ICNOT", // OK
											 "ICNOT2", // OK
											 "IX", // OK
											 "IY", // OK
											 "IZ", // OK
											 "IT", // OK
											 "IS", // OK
											 "IH", // OK
											 "IROT", // OK
											 "IREV", // OK
											 "DUMP", // OK
											 "QDUMP", // OK
											 "DW", // OK
											 "DL", // OK
											 "DB", // OK
											 "DS" // OK
											};
	
	public static boolean instrExists(String instr) {
		for (String ins : instructions) {
			if (instr.equals(ins))
				return true;
		}
		return false;
	}


}
