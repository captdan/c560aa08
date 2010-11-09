import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

/**
 * A class to hold all of the symbols encountered.
 * It is possible to add symbols, along with their information.
 * This information can then be retrieved. Also, a sorted list of the symbols
 * can be returned for printing purposes.
 * @author danielburnett
 *
 */
public class SymbolTable 
{

	/**
	 * A map to hold all of the symbols for the table.
	 * The label will be the key and the remaining information will be the value.
	 */
	static SortedMap<String, ArrayList<Object>> symb = new TreeMap<String, ArrayList<Object>>();
	int size = 0;

	/**
	 * Enmeration for the different possible types of usage for symbols.
	 * @author danielburnett
	 *
	 */
	public static enum Uses
	{
		DATA_LABEL,PROGRAM_NAME,EXTERNAL,EQU,ENT
	}
	
	/**
	 * 
	 * Module Name: addSymbol
	 * Description: Allows a symbol, along with its information, to be added to the table.
	 * Input Params: label
	 * 					Name of the symbol to be added
	 *		 		 addr
	 * 					Address of the symbol in hex
	 * 		 		 sub
	 * 					What the symbol is a substitute for, if at all.
	 * 		 		 use
	 * 					The way the symbol is being used.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Daniel Burnett
	 * Date of Installation: 10/3/2010
	 * Modifications: N/A
	 */
	public final void addSymbol(String label, String addr, String sub, Uses use) 
	{	
		if(symb.containsKey(label))
		{
			Parser.currentErrorArray.add(Parser.returnError(2));
		}
		else
		{
			ArrayList<Object> value = new ArrayList<Object>();
			value.add(addr);
			value.add(sub);
			value.add(use);
			symb.put(label, value);
			size++;
		}
	}
	
	/**
	 * 
	 * Module Name: changeToENT
	 * Description: used when a label is associated with an ENT directive in a .text section.
	 * Input Params: labelName
	 * 					Name of the label whose usage is changed.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns,Rakaan Kayali
	 * Date of Installation: 11/3/2010
	 * Modifications:
	 */
	public static void changeToENT(String labelName)
	{
		ArrayList<Object> temp = symb.get(labelName);
		symb.remove(labelName);
		temp.remove(2);
		temp.add(2,Uses.ENT);
		symb.put(labelName, temp);
		
	}

	/**
	 * 
	 * Module Name: getInfoFromSymbol
	 * Description: Given a certain symbol, receive the information associated with it.
	 * Input Params: label
	 * 					The symbol to get info about.
	 * Output Params: ArrayList<Object>
	 * 					Returns a string array with address, substitution and use, in that order.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: 
	 * Date of Installation: 10/3/2010
	 * Modifications: 
	 */
	public final ArrayList<Object> getInfoFromSymbol(String label) 
	{
		return symb.get(label);
	}

	
	/**
	 * 
	 * Module Name: isInTable
	 * Description: Checks the table to see if the desired label is present.
	 * Input Params: label
	 * 					The label to be checked in the table.
	 * Output Params: Boolean
	 * 					True if the label exists in the table, otherwise false.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Daniel Burnett
	 * Date of Installation: 10/3/2010
	 * Modifications: 
	 */
	public final boolean isInTable(String label) 
	{
		return symb.containsKey(label);
	}

	/**
	 * 
	 * Module Name: getSortedListOfSymbols
	 * Description:  Returns a sorted list containing all of the symbols in the table.
	 * Input Params: N/A
	 * Output Params: ArrayList<String>
	 * 					An array list, containing the symbols sorted alphabetically.
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Daniel Burnett
	 * Date of Installation: 10/3/2010
	 * Modifications: 
	 */
	public final ArrayList<String> getSortedListOfSymbols() 
	{
		return new ArrayList<String>(symb.keySet());
	}
	
	/**
	 * 
	 * Module Name: prettyFerret
	 * Description: Prints a "formatted" version of the symbol table.
	 * Input Params: N/A
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Daniel Burnett
	 * Date of Installation: 10/11/2010
	 * Modifications:
	 */
	public final void prettyFerret() 
	{
		try
		{
			FileWriter fileStream = new FileWriter("symboltable.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileStream);
			
			Iterator<Map.Entry<String, ArrayList<Object>>> it = symb.entrySet().iterator();
			while (it.hasNext()) 
			{
				Map.Entry<String, ArrayList<Object>> entry = it.next();
				bufferedWriter.write("------------------------------------------\n");
				bufferedWriter.write(entry.getKey() + "\t" + entry.getValue() + "\n");
				//System.out.println(entry.getKey() + "\t" + entry.getValue());
			}
			bufferedWriter.close();
			
			//int x = 0;
			
			/*
			for(CodeLine codeline : CodeLineArray)
			{
				bufferedWriter.write("------------------------------------------\n");
				bufferedWriter.write("CodeLine " + x + "\n");
				bufferedWriter.write(codeline.returnPrintString());
				x++;
			}
			bufferedWriter.close();
			*/
		}
		catch (Exception e)
		{
			//error
		}
		
	}

}
