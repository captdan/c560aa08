import java.util.*;

/**
 * A class to hold all of the symbols encountered.
 * It is possible to add symbols, along with their information.
 * This information can then be retrieved. Also, a sorted list of the symbols
 * can be returned for printing purposes.
 * @author danielburnett
 *
 */
public class SymbolTable {

	/**
	 * A map to hold all of the symbols for the table.
	 * The label will be the key and the remaining information will be the value.
	 */
	private SortedMap<String, String[]> symb = new TreeMap<String, String[]>();

	/**
	 * Allows a symbol, along with its information, to be added to the table.
	 * @param label Name of the symbol to be added
	 * @param addr Address of the symbol in hex
	 * @param sub What the symbol is a substitute for, if at all.
	 * @param use The way the symbol is being used.
	 */
	public final void addSymbol(String label, String addr, String sub, String use) {
		String[] value = new String[3];
		value[0] = addr;
		value[1] = sub;
		value[2] = use;
		symb.put(label, value);
	}

	/**
	 * Given a certain symbol, receive the information associated with it.
	 * @param label The symbol to get info about.
	 * @return Returns a string array with address, substitution and use, in that order.
	 */
	public final String[] getInfoFromSymbol(String label) {
		return symb.get(label);
	}

	/**
	 * Returns a sorted list containing all of the symbols in the table.
	 * @return An array list, containing the symbols sorted alphabetically.
	 */
	public final ArrayList<String> getSortedListOfSymbols() {
		return new ArrayList<String>(symb.keySet());
	}

}
