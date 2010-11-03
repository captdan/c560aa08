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

	/**
	 * Allows a symbol, along with its information, to be added to the table.
	 * @param label Name of the symbol to be added
	 * @param addr Address of the symbol in hex
	 * @param sub What the symbol is a substitute for, if at all.
	 * @param use The way the symbol is being used.
	 */
	public static enum Uses
	{
		DATA_LABEL,PROGRAM_NAME,EXTERNAL,EQU,ENT
	}
	
	/**
	 * Allows a symbol, along with its information, to be added to the table.
	 * @param label
	 * 			Name of the symbol to be added
	 * @param addr
	 * 			Address of the symbol in hex
	 * @param sub
	 * 			What the symbol is a substitute for, if at all.
	 * @param use
	 * 			The way the symbol is being used.
	 */
	public final void addSymbol(String label, String addr, String sub, Uses use) 
	{	
		/**if(this.symb.containsKey(label))
		{
			Error duplicatelabel = Parser.returnError(2);
			duplicatelabel.printError();
			//System.exit(1);
		}
		else{
		*/
		ArrayList<Object> value = new ArrayList<Object>();
		value.add(addr);
		value.add(sub);
		value.add(use);
		symb.put(label, value);
		//}
	}
	
	/**
	 * 
	 * Module Name: changeToENT
	 * Description: used when a label is associated with an ENT directive in a .text section.
	 * Input Params: String with the name of the label.
	 * Output Params: N/A
	 * Error Conditions Tested: N/A
	 * Error Messages Generated: N/A
	 * Original Author: Kermit Stearns,Rakaan Kayali
	 * Date of Installation: 11/3/2010
	 * Modifications: 
	 * @param labelName Name of the label whose usage is changed.
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
	 * Given a certain symbol, receive the information associated with it.
	 * @param label The symbol to get info about.
	 * @return Returns a string array with address, substitution and use, in that order.
	 */
	public final ArrayList<Object> getInfoFromSymbol(String label) 
	{
		return symb.get(label);
	}

	/**
	 * Checks the table to see if the desired label is present.
	 * @param label
	 * 			The label to be checked in the table.
	 * @return True if the label exists in the table, otherwise false.
	 */
	public final boolean isInTable(String label) 
	{
		return symb.containsKey(label);
	}

	/**
	 * Returns a sorted list containing all of the symbols in the table.
	 * @return An array list, containing the symbols sorted alphabetically.
	 */
	public final ArrayList<String> getSortedListOfSymbols() 
	{
		return new ArrayList<String>(symb.keySet());
	}
	
	/**
	 * Prints a "formatted" version of the symbol table.
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
