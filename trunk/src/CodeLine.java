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
	public int lineLength()
	{
		int length = 0;
		double intermediate = 0;
		if (this.instruction != null)
		{
			intermediate = 1;
		}
		else if (this.directive != null)
		{
			if (this.directive.equals("INT.DATA")||this.directive.equals("HEX.DATA")||
				this.directive.equals("BIN.DATA")|| this.directive.equals("NOP")||
					this.directive.equals("EQU.EXP"))
			{
				intermediate = 1;
			}
			
			else if (this.directive.equals("STR.DATA"))
			{
				intermediate = 1;
				String stringLine = this.toString();
				String[] dataVariables = stringLine.split(" ");
				
				if (dataVariables[3].length()/4>1)
				{
					intermediate = (dataVariables[3].length()/4);
				}
				
				if (intermediate>4)
				{
				//System.out.println("Error too many characters");	
				}
				
			}
			else if (this.directive.equals("EQU"))
			{
				intermediate = 1;
				String stringLine = this.toString();
				String[] dataVariables = stringLine.split(" ");
				if (dataVariables[3].length()/4>1)
				{
					intermediate = (dataVariables[3].length()/4);
				}
				if (intermediate>8)
				{
				//System.out.println("Error too many characters");	
				}
				
			}
		}
		length = (int)Math.ceil(intermediate);
		return length;
	}
}
