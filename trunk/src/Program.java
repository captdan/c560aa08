import java.util.ArrayList;

public class Program 
{

	/**
	 * Module Name: Program
	 * Description: A valid SAL560 program with all of its complementary tables necessry to run the program.
	 * Input Params: N/A
	 * Output Params: N/A
	 * Error Conditions: Tested: N/A
	 * Error Messages: Generated: N/A
	 * Original Author: Kermit Stearns, Bobby Schmidt, Rakaan Kayali, Dan Burnett, Oscar Flores
	 * Date of Installation: 11/3/2010
	 * Modifications:
	 * @param args
	 */
	
	public String programName = "";
	public int startLocation;
	public int executionStart;
	public int programLength;
	public boolean debugMode;
	public ArrayList<CodeLine> CodeLineArray = new ArrayList<CodeLine>();
	public SymbolTable symbolTable;
	public LiteralTable literalTable;
	
	public Program (String p, int start, int execstart, boolean debugModeValue,int length, ArrayList<CodeLine> a, SymbolTable s, LiteralTable l)
	{
		programName = p;
		startLocation = start;
		executionStart = execstart;
		programLength = length;
		CodeLineArray = a;
		symbolTable = s;
		literalTable = l;
		debugMode = debugModeValue;
	}

	
}
