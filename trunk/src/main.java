import java.util.ArrayList; 
import java.util.StringTokenizer;

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
		//Create an instance of CodeLine Supplying it with original line of code.
		CodeLine cl = new CodeLine(lineOfCode);
		
		//Check to see if the line contains a comment if so parse it.
		String[] result = lineOfCode.split("|");
		//Grab anything that is before the comment and set it aside to be parsed.
		String Instruction = result[0];
		
		//Check to see if a comment exists.
		if(result.length > 1)
		{
			result[0] = "";
			cl.comment = joinStringArray(result,"");
		}
		
		StringTokenizer st = new StringTokenizer(lineOfCode);
		//st.hasMoreTokens() = true
		
		while (st.hasMoreTokens()) 
		{
	         System.out.println(st.nextToken());
	    }

		return cl;

	}

	//Simple Function that joins a String array by a delimiter.
	public static String joinStringArray(String[] stringArray, String delimiter)
	{
		String totalString = stringArray[0];
		for(int x=1;x<stringArray.length;x++)
		{
			totalString += (delimiter + stringArray[x]);
		}
		return totalString;
	}
}
