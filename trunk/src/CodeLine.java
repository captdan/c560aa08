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
	public Integer length = 0; //This should not be static... in instance needs to be created for to use it

	// Constructor
	CodeLine(String lineOfCode) 
	{
		this.originalLineOfCode = lineOfCode;
	}
	public static int lineLength(CodeLine line)
	{
		int length = 0;
		
		if (line.instruction != null)
		{
			length = 1;
		}
		else if (line.directive != null)
		{
			if (line.directive.equals("INT.DATA")||line.directive.equals("HEX.DATA")||
				line.directive.equals("BIN.DATA")|| line.directive.equals("NOP")||line.directive.equals("EQU")||
					line.directive.equals("EQU.EXP"))
			{
				length = 1;
			}
			
			else if (line.directive.equals("STR.DATA"))
			{
				length = 1;
				String stringLine = line.toString();
				String[] dataVariables = stringLine.split("\t");
				if (dataVariables[3].length()/4>1)
				{
					length = (dataVariables[3].length()/4);
				}
			}
		}
		return length;
	}
}
