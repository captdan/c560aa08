/**
 * 
 */

/**
 * @author Shaka
 *
 */
public class Test {

	/**
	 * Module Name:
	 * Description:
	 * Input Params:
	 * Output Params:
	 * Error Conditions Tested:
	 * Error Messages Generated:
	 * Original Author:
	 * Date of Installation:
	 * Modifications:
	 * @param args
	 */
	public static void main(String[] args) {
		String t = "0000000000000101";
		
		int o = -24;
		ALU ALU = new ALU();
		System.out.println(ALU.GetIntegerFromTwosComplementSigned(t));
		
		System.out.println(ALU.intToHex(o));
		System.out.println("ADD TEST");
		String binaryInstruction2 = "11111111111111111111111111111111";
		// 1
		// -1
		String imm = "10001";
		
		// -01111
		// -15
		
		
		int b = ALU.ADD(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// 0111011
		// 69 ^^^^
		imm = "00001";
		// 1
		b = ALU.ADD(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.ADD(binaryInstruction2, imm);
		binaryInstruction2 = "0000";
		// -0111011
		// -59 ^^^^
		imm = "0000";
		// -01111
		// -15
		b = ALU.ADD(binaryInstruction2, imm);
		
		
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("SUB TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.SUB(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.SUB(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// -0111011
		// -59 ^^^^
		imm = "00001";
		// 01111
		// -15
		b = ALU.SUB(binaryInstruction2, imm);
		
		binaryInstruction2 = "00000";
		// -0111011
		// -59 ^^^^
		imm = "00000";
		// -01111
		// -15
		b = ALU.SUB(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("MUL TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.MUL(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.MUL(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// -0111011
		// -59 ^^^^
		imm = "00001";
		// 01111
		// -15
		b = ALU.MUL(binaryInstruction2, imm);
		
		binaryInstruction2 = "00000";
		// -0111011
		// -59 ^^^^
		imm = "00000";
		// -01111
		// -15
		b = ALU.MUL(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("DIV TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.DIV(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.DIV(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// -0111011
		// -59 ^^^^
		imm = "00001";
		// 01111
		// -15
		b = ALU.DIV(binaryInstruction2, imm);
		
		binaryInstruction2 = "00000";
		// -0111011
		// -59 ^^^^
		imm = "00000";
		// -01111
		// -15
		b = ALU.DIV(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("ADDU TEST");
		binaryInstruction2 = "01000101";
		// 01000101
		// 69 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.ADDU(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.ADDU(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// -0111011
		// -59 ^^^^
		imm = "00001";
		// 01
		// 1
		b = ALU.ADDU(binaryInstruction2, imm);
		
		binaryInstruction2 = "00000";
		// -0111011
		// -59 ^^^^
		imm = "00000";
		// -01111
		// -15
		b = ALU.ADDU(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("SUBU TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.SUBU(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.SUBU(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// -0111011
		// -59 ^^^^
		imm = "00001";
		// 01111
		// -15
		b = ALU.SUBU(binaryInstruction2, imm);
		
		binaryInstruction2 = "00000";
		// -0111011
		// -59 ^^^^
		imm = "00000";
		// -01111
		// -15
		b = ALU.SUBU(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("MULU TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.MULU(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.MULU(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// -0111011
		// -59 ^^^^
		imm = "00001";
		// 01111
		// -15
		b = ALU.MULU(binaryInstruction2, imm);
		
		binaryInstruction2 = "00000";
		// -0111011
		// -59 ^^^^
		imm = "00000";
		// -01111
		// -15
		b = ALU.MULU(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("DIVU TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.DIVU(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.DIVU(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// -0111011
		// -59 ^^^^
		imm = "00001";
		// 01111
		// -15
		b = ALU.DIVU(binaryInstruction2, imm);
		
		binaryInstruction2 = "00000";
		// -0111011
		// -59 ^^^^
		imm = "00000";
		// -01111
		// -15
		b = ALU.DIVU(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("PWR TEST");
		binaryInstruction2 = "0101";
		//0011
		// 3 ^^^^
		imm = "101";
		// -11
		// -3
		b = ALU.PWR(binaryInstruction2, imm);
		
		binaryInstruction2 = "0101";
		///0011
		// 3 ^^^^
		imm = "001";
		// 11
		// 3
		b = ALU.PWR(binaryInstruction2, imm);
		
		binaryInstruction2 = "1101";
		//0011
		// 3 ^^^^
		imm = "101";
		// -11
		// -3
		b = ALU.PWR(binaryInstruction2, imm);
		
		binaryInstruction2 = "00000";
		//00000
		//00000
		imm = "00000";
		// 00000
		// 0000
		b = ALU.PWR(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("ADDI TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.ADDI(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.ADDI(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// -0111011
		// -59 ^^^^
		imm = "00001";
		// 01111
		// -15
		b = ALU.ADDI(binaryInstruction2, imm);
		
		binaryInstruction2 = "0000000";
		// -0111011
		// -59 ^^^^
		imm = "00000";
		// -01111
		// -15
		b = ALU.ADDI(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("SUBI TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.SUBI(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.SUBI(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// -0111011
		// -59 ^^^^
		imm = "00001";
		// 01111
		// -15
		b = ALU.SUBI(binaryInstruction2, imm);
		
		binaryInstruction2 = "0000000";
		// -0111011
		// -59 ^^^^
		imm = "00000";
		// -01111
		// -15
		b = ALU.SUBI(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("MULI TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.MULI(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.MULI(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "00001";
		// 01111
		// 15
		b = ALU.MULI(binaryInstruction2, imm);
		
		binaryInstruction2 = "0000000";
		//0000000000
	
		imm = "00000";
		// 000000000
		b = ALU.MULI(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("DIVI TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.DIVI(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.DIVI(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "00001";
		// 01111
		// 15
		b = ALU.DIVI(binaryInstruction2, imm);
		
		binaryInstruction2 = "0000000";
		//0000000000
	
		imm = "00000";
		// 000000000
		b = ALU.DIVI(binaryInstruction2, imm);
		
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("ADDIU TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.ADDIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.ADDIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "00001";
		// 01111
		// 15
		b = ALU.ADDIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "0000000";
		//0000000000
	
		imm = "00000";
		// 000000000
		b = ALU.ADDIU(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("SUBIU TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.SUBIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.SUBIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "00001";
		// 01111
		// 15
		b = ALU.SUBIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "0000000";
		//0000000000
	
		imm = "00000";
		// 000000000
		b = ALU.SUBIU(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("MULIU TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.MULIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.MULIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "00001";
		// 01111
		// 15
		b = ALU.MULIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "0000000";
		//0000000000
	
		imm = "00000";
		// 000000000
		b = ALU.MULIU(binaryInstruction2, imm);
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------
		System.out.println("DIVIU TEST");
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.DIVIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "11000101";
		// -0111011
		// -59 ^^^^
		imm = "10001";
		// -01111
		// -15
		b = ALU.DIVIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "01000101";
		// 0111011
		// 59 ^^^^
		imm = "00001";
		// 01111
		// 15
		b = ALU.DIVIU(binaryInstruction2, imm);
		
		binaryInstruction2 = "0000000";
		//0000000000
	
		imm = "00000";
		// 000000000
		b = ALU.DIVIU(binaryInstruction2, imm);
		

		
	}

}
