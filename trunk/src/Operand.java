
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
	
	/**
	 * Directive Operand checking:
	 * 	NUMBER, BINARY, HEX, STRING, LABEL, LABELREF, BOOLEAN, EXP
	 */
	static boolean validLabel (String label) 
	{
		//need to flesh this out, but each method to check operands should just return a boolean 
		return false;
	}
}
