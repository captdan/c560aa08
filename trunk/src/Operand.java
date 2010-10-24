
public class Operand
{
	public String operand;
	
	public Operand(String operandValue)
	{
		operand = operandValue;
	}
	public Operand() 
	{

	}
	
	/**Instruction Operand Checking:
	 * 	REGISTER, IMMEDIATE, ADDRESS, BIT, BITS, NUMBER, IO
	 * 
	 * @param label
	 * @return
	 */
	static boolean isValidInstructionRegister (String register) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidInstructionImmediate (String immediate) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidInstructionAddress (String address) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidInstructionBit (String bit) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidInstructionBits (String bits) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidInstructionNumber (String number) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidInstructionIO (String io) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	
	
	/**
	 * Directive Operand checking:
	 * 	NUMBER, BINARY, HEX, STRING, LABEL, LABELREF, BOOLEAN, EXP
	 */
	
	static boolean isValidDirectiveNumber (String number) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidDirectiveBinary (String binary) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidDirectiveHex (String hex) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidDirectiveString (String string) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidDirectiveLabel (String label) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidDirectiveLabelRef (String lableref) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidDirectiveBoolean (String bool) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
	static boolean isValidDirectiveExp (String exp) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
}
