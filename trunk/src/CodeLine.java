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

	// Constructor
	CodeLine(String lineOfCode) 
	{
		this.originalLineOfCode = lineOfCode;
	}
}
