import java.util.ArrayList; 

public class main
{

	/**
	 * @param args
	 */
	public static ArrayList<CodeLine> CodeLineArray = new ArrayList<CodeLine>();
	public static void main(String[] args)
	{
		System.out.println("test");
		
		// Create a new instance of CodeLine class

		CodeLineArray.add(parseCodeLine("sdaf"));
	}
	
	//This is where we will parse each line of code.
	public static CodeLine parseCodeLine(String lineOfCode) 
	{
		CodeLine cl = new CodeLine(lineOfCode);
		
		return cl;

	}

}
