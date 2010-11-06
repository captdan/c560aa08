import java.util.ArrayList;

/**
 * A class to hold all of the literals encountered.
 * Literals are added along with the address at which they occur.
 * A list of String[] containing the literals and their addresses can be obtained.
 * @author daniel burnett
 *
 */
public class LiteralTable {
	/**
	 * Holds all of the literals and their addresses.
	 */
	private ArrayList<String[]> tableOfLiterals = new ArrayList<String[]>();

	/**
	 * 
	 * Module Name: addLiteral.
	 * Description: Allows a literal along with its corresponding address to be added to the table.
	 * Input Params: literal
	 * 			The literal to be added.
	 * 				 addr
	 * 			The address of the literal being added.
	 * Output Params: None.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: daniel burnett
	 * Date of Installation: 10/07/2010.
	 * Modifications: None.
	 */
	public final void addLiteral(String literal, String addr) {
		String[] temp = new String[2];
		temp[0] = literal;
		temp[1] = addr;
		tableOfLiterals.add(temp);
	}

	/**
	 * 
	 * Module Name: getLiteralList.
	 * Description: Returns a list of the literals and their addresses.
	 * Input Params: None.
	 * Output Params: None.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: daniel burnett
	 * Date of Installation: 10/07/2010.
	 * Modifications: None.
	 */
	public final ArrayList<String[]> getLiteralList() {
		return tableOfLiterals;
	}
}
