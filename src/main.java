import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
		//System.out.println("test");
		ArrayList<String> linesOfCode = readFileToArrayList("asmCode1.txt");
		for(String lineOfCode : linesOfCode)
		{
			CodeLineArray.add(parseCodeLine(lineOfCode));
		}
	}
	
	//This is where we will parse each line of code.
	public static CodeLine parseCodeLine(String lineOfCode) 
	{
		//Create an instance of CodeLine Supplying it with original line of code.
		CodeLine cl = new CodeLine(lineOfCode);
		
		//Check to see if the line contains a comment if so parse it.
		String[] result = lineOfCode.split("|");
		//Grab anything that is before the comment and set it aside to be parsed.
		String lineOfCodeMinusComment = result[0];
		
		//Check to see if a comment exists.
		if(result.length > 1)
		{
			result[0] = "";
			cl.comment = joinStringArray(result,""); 
			//If more than one "|" then join them all as one comment
		}
		
		StringTokenizer st = new StringTokenizer(lineOfCodeMinusComment);
		if (st.hasMoreTokens() == true) 
		{
			String Instruction = st.nextToken();
			
			
			
			
		}

		return cl;

	}

	//Function that joins a String array by a delimiter.
	public static String joinStringArray(String[] stringArray, String delimiter)
	{
		String totalString = stringArray[0];
		for(int x=1;x<stringArray.length;x++)
		{
			totalString += (delimiter + stringArray[x]);
		}
		return totalString;
	}
	
	/*
	 * Function that returns an ArrayList of Strings where each
	 * line in the the file is a string.
	 */
	public static ArrayList<String> readFileToArrayList(String fileName)
	{
		ArrayList<String> linesOfCode = new ArrayList<String>();
	    try 
		{
	    FileInputStream fileInputStream = new FileInputStream(fileName);
	    DataInputStream dataInputStream = new DataInputStream(fileInputStream);
	    BufferedReader buffer = new BufferedReader(new InputStreamReader(dataInputStream));
	    
	    String lineOfCode;
	    
	    while (( lineOfCode = buffer.readLine()) != null)   
	    {
	    	linesOfCode.add(lineOfCode);
	    }

	    dataInputStream.close();
		} 
	    catch (FileNotFoundException e) 
		{
	    	System.out.println("File Not Found");
			e.printStackTrace();
		} 
	    catch (IOException e) 
		{
	    	System.out.println("Error Reading File");
			e.printStackTrace();
		}
	    return linesOfCode;
	}
}
