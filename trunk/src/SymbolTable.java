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
	private SortedMap<String, ArrayList<Object>> symb = new TreeMap<String, ArrayList<Object>>();

	/**
	 * Allows a symbol, along with its information, to be added to the table.
	 * @param label Name of the symbol to be added
	 * @param addr Address of the symbol in hex
	 * @param sub What the symbol is a substitute for, if at all.
	 * @param use The way the symbol is being used.
	 */
	public static enum Uses
	{
		DATA_LABEL,PROGRAM_NAME,EXTERNAL,EQU
	}
	public final void addSymbol(String label, String addr, String sub, Uses use) 
	{
		ArrayList<Object> value = new ArrayList<Object>();
		value.add(addr);
		value.add(sub);
		value.add(use);
		symb.put(label, value);
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
		Iterator<Map.Entry<String, ArrayList<Object>>> it = symb.entrySet().iterator();
		while (it.hasNext()) 
		{
			Map.Entry<String, ArrayList<Object>> entry = it.next();
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
		
	}

}
