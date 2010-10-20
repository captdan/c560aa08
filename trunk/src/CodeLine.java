import java.util.ArrayList;
//import java.util.HashSet;

//CodeLine.java
class CodeLine 
{
	// Define Variables
	public String originalLineOfCode = "";
	public String comment = "";
	public Instruction instruction = null;
	public Directive directive = null;
	public ArrayList<Error> errors = new ArrayList<Error>();
	
	/**
	 * This will be the field where each line of code's length is stored. 
	 * This field is then read from each CodeLine object in our Main Parser and added to the global Program Counter.
	 */

	// Constructor
	CodeLine(String lineOfCode) 
	{
		this.originalLineOfCode = lineOfCode;
	}
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
	public String returnPrintString()
	{
		String returnString = "";
		returnString += "Original Line Of Code: " + this.originalLineOfCode + "\n";
		returnString += "Line Comment: " + this.comment + "\n";
		returnString += "Line Length: " + Integer.toString(this.lineLength()) + "\n";
		returnString += "Errors:\n";
		for(Error error : this.errors)
		{
			returnString += "\t" + Integer.toString(error.number) + "\t" + error.message.toString() + "\t" + error.correction + "\n";
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
