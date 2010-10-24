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
	 * This function returns the length in terms of increments to the PC counter caused by the line of code
	 * the function is called on. 
	 * @author Rakaan Kayali and Kermit Stearns.
	 * Modified: changed global variable length by removing its 'static' label. 
	 * Now an instance must be created before it is used. Additionally, 
	 * STR.DATA and EQU are now handled separately.
	 * Date Modified: 10/10/2010, by Kermit Stearns and Rakaan Kayali.
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
					//System.out.println("TREWQ: " + stringLine);
					//System.out.println(stringLine);
					length = (int) Math.ceil(stringLine.length()/4.0);
					
					if (length>4)
					{
					//System.out.println("Error too many characters");	
					}
				}
				else
				{
					//System.out.println("TREWQ: " + stringLine);
					//Error
				}
				//System.out.println(this.directive.operandArray.get(0).operand);
				
			}
			else if (this.directive.directiveName.equals("MEM.SKIP"))
			{
				length = Integer.valueOf(this.directive.operandArray.get(0).operand);
				//System.out.println("QWER: " + Integer.valueOf(this.directive.operandArray.get(0).operand));
			}

		}
		//length = (int)Math.ceil(intermediate);
		return length;
	}
	/**
	 * This method prints out the original line of code along with the line comment the line length and 
	 * all applicable errors.
	 */
	public String returnPrintString()
	{
		String returnString = "";
		returnString += "Original Line Of Code: " + this.originalLineOfCode + "\n";
		returnString += "Line Comment: " + this.comment + "\n";
		returnString += "Line Length: " + Integer.toString(this.lineLength()) + "\n";
		returnString += "Errors:\n";
		for(Error error : this.errors)
		{
			//System.out.println("QWER");
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
