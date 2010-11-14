import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Kerovesmo
 *
 */
public class AssemblyListing {
	private Program p;
	int statementNumber = 1;
	
	
	public AssemblyListing(Program program)
	{
		p = program;
		
	}
	
	/**
	 * 
	 * Module Name: makeRecord
	 * Description: Takes one CodeLine object and generates an ArrayList<String> 
	 * 				with the appropriate values.
	 * Input Params: CodeLine c
	 * Output Params: ArrayList<String>
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns
	 * Date of Installation: 11/8/2010
	 * Modifications:
	 * @param c CodeLine object to be made into AssemblyListing record.
	 * @return ArrayList<String> with values of AssemblyListing.
	 */
	public ArrayList<String> makeRecord(CodeLine c)
	{
		
		ArrayList<String> record = new ArrayList<String>();
		record.add(String.valueOf(Integer.toHexString(c.PC)));
		
		if(c.directive != null)
		{
			if(c.errors.size() == 0)
			{
				record.add(c.directive.returnHexCodeLine(c));
			}
			if(c.directive.operandArray.size() > 0)
			{
				record.add(String.valueOf(c.directive.operandArray.get(0).relocationType));
			}
			else
			{
				record.add("");
			}
			
			record.add(String.valueOf(statementNumber));
			record.add(c.originalLineOfCode);
		}
		
		if(c.instruction != null)
		{
			if(c.errors.size() == 0)
			{
				record.add(c.instruction.returnHexCodeLine());
			}
			record.add(String.valueOf(c.instruction.operandsArray.get(0).relocationType));
			record.add(String.valueOf(statementNumber));
			record.add(c.originalLineOfCode);
		}
		
		statementNumber++;
		return record;
	}
	/**
	 * 
	 * Module Name: prettyFerret2
	 * Description: Writes the AssemblyListing file to a .txt.
	 * Input Params: N/A
	 * Output Params: AssemblyListing.txt
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns
	 * Date of Installation: 11/8/2010
	 * Modifications: N/A
	 */
	public void prettyFerret3()
	{
		FileWriter fileStream;
		try
		{
			fileStream = new FileWriter("AssemblyListing.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileStream);
			
			bufferedWriter.write("LOC" + "\t" + "OBJ CODE" + " " + "A/R/E" + " " + "STMT" + " " + "SOURCE STATEMENT" + "\n" );
	
			for(CodeLine c : p.CodeLineArray)
			{
				
				ArrayList<String> record = makeRecord(c);
				
				bufferedWriter.write(joinStringArray(record,"\t") + "\n");
				
				//Check to see if there are any errors associated with the line of code.
				if(c.errors.size() > 0)	
				{
					//bufferedWriter.write(c.originalLineOfCode);
					for(Error e : c.errors)
					{
						bufferedWriter.write("\t" + String.valueOf(e.number) + "\t" + e.message + "\t" + e.correction + "\n");
					}
					
				}
			}
			bufferedWriter.close();
		}
		catch (IOException e)
		{
			System.err.println("Unable to write Assembly Listing file!");
		}
	}
	
	/**
	 * 
	 * Module Name: joinStringArray
	 * Description: Joins a string array into a single string.
	 * Input Params: stringArray
	 * 					The array to be joined.
	 * 				 delimiter
	 * 					The delimiter character.
	 * Output Params: String
	 * 					The joined array.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns
	 * Date of Installation: 11/5/2010
	 * Modifications: 
	 */
	private String joinStringArray(ArrayList<String> stringArray, String delimiter) 
	{
		String totalString = stringArray.get(0);
		
		for (int x = 1; x < stringArray.size(); x++) 
		{
			totalString += (delimiter + stringArray.get(x));
		}
		return totalString;
	}
}
