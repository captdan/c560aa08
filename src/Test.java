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
		String binaryInstruction2 = "000101";
		String a = binaryInstruction2.substring(0,5);
		System.out.println(a);
		int OPCODE = Integer.parseInt(binaryInstruction2.substring(0,6),2);
		System.out.println(OPCODE);
	}

}
