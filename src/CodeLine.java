import java.util.ArrayList;

//CodeLine.java
class CodeLine 
{
	// Define Variables
	public String originalLineOfCode = "";
	public String comment = "";
	public ArrayList<Operand> operands = new ArrayList<Operand>();
	public Instruction instruction = new Instruction();
	public ArrayList<Error> errors = new ArrayList<Error>();

	// Constructor
	CodeLine(String lineOfCode) 
	{
		originalLineOfCode = lineOfCode;
	}

	public void test() 
	{
		System.out.println("test");
	}
}
