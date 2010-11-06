import java.util.ArrayList;
/**
 * @author Robert Schmidt.
 * This class is an object that allows us to store a line of code and determine certain
 * attributes of that codeline such as comment, possible instruction, directives or errors. 
 */

//CodeLine.java
class CodeLine 
{
	/**
	 * originalLineOfCode stores the line of code from the source program.
	 */
	public String originalLineOfCode = "";
	/**
	 * Stores the comment associated with the originalLineOfCode.
	 */
	public String comment = "";
	/**
	 * Stores an instruction if present in the originalLineOfCode.
	 */
	public Instruction instruction = null;
	/**
	 * Stores a directive if present in the originalLineOfCode.
	 */
	public Directive directive = null;
	/**
	 * Holds the errors found in the codeline.
	 */
	public ArrayList<Error> errors = new ArrayList<Error>();
	public scopeOptions scope = CodeLine.scopeOptions.A;
	public enum scopeOptions
	{
		A,R,E
	}
	
	public int PC = 0;
	
	/**
	 * This constructs a CodeLine object from a string thats passed in.
	 * @param lineOfCode
	 * 			The string that is to be converted to a lineOfCode.
	 * 
	 */
	CodeLine(String lineOfCode) 
	{
		this.originalLineOfCode = lineOfCode;
	}
	/**
	 * 
	 * Module Name: lineLength
	 * Description: This function returns the length in terms of increments to the PC counter caused by the line of code
	 * the function is called on. 
	 * Input Params: None.
	 * Output Params: None.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Rakaan Kayali and Kermit Stearns.
	 * Date of Installation: 10/10/2010.
	 * Modifications: Modified: changed global variable length by removing its 'static' label. 
	 * Now an instance must be created before it is used. Additionally, 
	 * STR.DATA and EQU are now handled separately.
	 * @return returns the length in terms of increments to the PC counter caused by the line of code
	 * the function is called on.  
	 */
	public int lineLength()
	{
		int length = 0;
		if (this.instruction != null)
		{
			length = 1;
		}
		else if (this.directive != null)
		{
			if (this.directive.directiveName.equals("INT.DATA")||this.directive.directiveName.equals("HEX.DATA")||
					this.directive.directiveName.equals("BIN.DATA")|| this.directive.directiveName.equals("NOP")||
					this.directive.directiveName.equals("EQU.EXP") || this.directive.directiveName.equals("ADR.DATA"))
			{
				length = 1;
			}
			else if (this.directive.directiveName.equals("STR.DATA"))
			{	 
				String stringLine = this.directive.operandArray.get(0).operand;
				if(this.directive.operandArray.get(0).operand.startsWith("'") && this.directive.operandArray.get(0).operand.endsWith("'"))
				{
					stringLine = stringLine.substring(1, stringLine.length()-1);
					length = (int) Math.ceil(stringLine.length()/4.0);
				}
			}
			else if (this.directive.directiveName.equals("MEM.SKIP"))
			{
				length = Integer.valueOf(this.directive.operandArray.get(0).operand);
			}
			else
			{
				length = 0;
			}
		}
		return length;
	}
	/**
	 * 
	 * Module Name:	returnPrintString.
	 * Description: This method prints out the original line of code along with the line comment the line length and 
	 * all applicable errors.
	 * Input Params: None.
	 * Output Params: A String containing the original line plus any applicable errors and line length in the PC 
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None
	 * Original Author: Robert Schmidt.
	 * Date of Installation: 10/12/2010.
	 * Modifications: None.
	 * @return
	 */
	public String returnPrintString()
	{
		String returnString = "";
		returnString += "Original Line Of Code: " + this.originalLineOfCode + "\n";
		returnString += "Line Comment: " + this.comment + "\n";
		returnString += "Line Length: " + Integer.toString(this.lineLength()) + "\n";
		returnString += "Error Count: " + this.errors.size() + "\n";
		returnString += "Errors:\n";
		for(Error error : this.errors)
		{
			returnString += "\t" + error.number + "\t" + error.message + "\t" + error.correction + "\n";
		}
		if (this.instruction != null)
		{
			returnString += this.instruction.returnPrintString() + "\n";
		}
		if (this.directive != null)
		{
			returnString += this.directive.returnPrintString() + "\n";
		}		
		return returnString;
	}
}
