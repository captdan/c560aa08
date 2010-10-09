import java.util.ArrayList;
import java.util.HashSet;

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
	public static Integer length;

	// Constructor
	CodeLine(String lineOfCode) 
	{
		this.originalLineOfCode = lineOfCode;
	}
}
