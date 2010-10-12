import java.util.ArrayList;

/**
 * A class to hold all of the literals encountered.
 * Literals are added along with the address at which they occur.
 * A list of String[] containing the literals and their addresses can be obtained.
 * @author danielburnett
 *
 */
public class LiteralTable {
	/**
	 * Holds all of the literals and their addresses.
	 */
	private ArrayList<String[]> table = new ArrayList<String[]>();

	/**
	 * Allows a literal along with its corresponding address to be added to the table.
	 * @param literal
	 * 			The literal to be added.
	 * @param addr
	 * 			The address of the literal being added.
	 */
	public final void addLiteral(String literal, String addr) {
		String[] temp = new String[2];
		temp[0] = literal;
		temp[1] = addr;
		table.add(temp);
	}

	/**
	 * Returns a list of the literals and their addresses.
	 * @return The list to be returned.
	 */
	public final ArrayList<String[]> getLiteralList() {
		return table;
	}
}
