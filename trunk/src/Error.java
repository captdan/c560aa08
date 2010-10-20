/**
 * 
 * @author C560aa08
 * Container class for Error object to report to user in case a syntax or boundary error occurs in the code.
 */
public class Error
{
	/**
	 * num
	 *            : integer based number associated with an error.

	 *  			           
	 */
	public int number;
	/**
	 *mes
	 *     : String containing the proper description for an error event.
	 */
	
	public String message;
	/**
	 *  correctionValue
	 *  		  :String containing a suggestion to fix the current error
	*/
	public String correction;

	/**
	 * @param num
	 *            : integer based number associated with an error.
	 * @param mes
	 *            : String containing the proper description for an error event.
	 *  @param correctionValue
	 *  		  :String containing a suggestion to fix the current error
	 *  			           
	 */
	public final void CreateError(final int num, String mes, String correctionValue) 
	{
		this.correction = correctionValue;
		this.number = num;
		this.message = mes;

	}
	/**
	 * Prints the contents of an Error object as a report to the user.
	 */
	public final void printError(){
		System.out.println("Error number " + this.number + ":\t");
		System.out.print(this.message);
		System.out.println(this.correction);
		
	}

}
