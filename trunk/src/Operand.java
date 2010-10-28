
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
		//Valid register 0-7
		return false;
	}
	static boolean isValidInstructionImmediate (String immediate) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//can have a single '+' or '-' preceeding it, must fit signed value in 16 bits (-231<=x<=230)
		return false;
	}
	static boolean isValidInstructionAddress (String address) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//has to be greater than starting LC, less than 65536
		return false;
	}
	static boolean isValidInstructionBit (String bit) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//operand should have 3 fields, each containing 1 or 0
		return false;
	}
	static boolean isValidInstructionBits (String bits) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//# of bits to be shifted in a shift instruction (makes sense to limit this to 0-15???)
		return false;
	}
	static boolean isValidInstructionNumber (String number) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//words of memory to be reserved for IO instructions...should we limit this? 
		return false;
	}
	
	
	/**
	 * Directive Operand checking:
	 * 	NUMBER, BINARY, HEX, STRING, LABEL, LABELREF, BOOLEAN, EXP
	 */
	
	static boolean isValidDirectiveNumber (String number) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		// -231<=x<=230
		return false;
	}
	static boolean isValidDirectiveBinary (String binary) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//32 binary digits
		return false;
	}
	static boolean isValidDirectiveHex (String hex) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//max of 8 hex digits, fill in with signed value in front (either 0 or F)
		return false;
	}
	static boolean isValidDirectiveString (String string) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//length is limited to 16 characters (this was our choice)
		return false;
	}
	static boolean isValidDirectiveLabel (String label) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//A-Z,a-z,_,0-9
		return false;
	}
	static boolean isValidDirectiveLabelRef (String lableref) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//label must already be in our Symbol Table at this point (function in SymbolTable class)
		return false;
	}
	static boolean isValidDirectiveBoolean (String bool) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//Boolean debug flag, length 1, either 1 or 0
		return false;
	}
	static boolean isValidDirectiveExp (String exp) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		//valid expression(include handling * notation)
		return false;
	}
}
