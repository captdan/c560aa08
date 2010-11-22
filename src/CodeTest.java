
public class CodeTest {
public static void main(String[] args){
	System.out.println("ADD TEST");
	String binaryInstruction2 = "11111111111111111111111111111111";
	// 00000000000000000000000000001
	// -1
	
	String imm = "10001";
	// -01111
	// -15
	ALU ALU = new ALU();
	String hex = ALU.binToHex32bits(imm);
	System.out.println(hex);
	imm = ALU.hexToBin(hex);
	System.out.println(imm);
	
	System.out.println();
	ALU.ADD(imm, imm);
	int b = ALU.ADD(binaryInstruction2, imm);
	
//	System.out.println(d);
	
}
}
