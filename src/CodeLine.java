import java.util.ArrayList;
import java.util.HashSet;

//CodeLine.java
class CodeLine 
{
	// Define Variables
	private String originalLineOfCode = "";
	public String comment = "";
	public ArrayList<Operand> operands = new ArrayList<Operand>();
	public Instruction instruction = new Instruction();
	public ArrayList<Error> errors = new ArrayList<Error>();

	// Constructor
	CodeLine(String lineOfCode) 
	{
		this.originalLineOfCode = lineOfCode;
	}
	
	public boolean hasDirective ()
	{
		HashSet<String> directives = new HashSet<String>();
		directives.add(".START");
		directives.add(".END");
		directives.add(".DATA");
		directives.add(".TEXT");
		directives.add("INT.DATA");
		directives.add("STR.DATA");
		directives.add("HEX.DATA");
		directives.add("BIN.DATA");
		directives.add("ADR.DATA");
		directives.add("ADR.EXP");
		directives.add("ENT");
		directives.add("EXT");
		directives.add("NOP");
		directives.add("EXEC.START");
		directives.add("MEM.SKIP");
		directives.add("EQU");
		directives.add("EQU.EXP");
		directives.add("RESET.LC");
		directives.add("DEBUG");
		
		String copy = this.originalLineOfCode;
		String [] splitCopy = copy.split("");
		if (directives.contains(splitCopy[0]))
		{
			return true;
		}
		return false;
	}
}
