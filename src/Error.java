/**
 * 
 * @author Oscar Flores
 * Container class for Error object to report to user in case a syntax or boundary error occurs in the code.
 */
public class Error
{
	/**
	 * number
	 *            : integer based number associated with an error.

	 *  			           
	 */
	public int number;
	/**
	 *message
	 *     : String containing the proper description for an error event.
	 */
	
	public String message;
	/**
	 *  correction
	 *  		  :String containing a suggestion to fix the current error
	*/
	public String correction;

	/**
	 * 
	 * Module Name: CreateError.
	 * Description: Constructor for an Error object.
	 * Input Params: num
	 * 					the number code corresponding to the error being created.
	 * 				 mes
	 * 					the message associated with the error code.
	 * 				 correctionValue
	 * 					the recommended fix for the given error code.
	 * Output Params: None.
	 * Error Conditions Tested: None.
	 * Error Messages Generated: None.
	 * Original Author: Oscar Flores
	 * Date of Installation: 10/02/2010.
	 * Modifications: None.
	 */
	public final void CreateError(final int num, String mes, String correctionValue) 
	{
		this.correction = correctionValue;
		this.number = num;
		this.message = mes;

	}
	/**
	 * Constructor for Error Object.
	 * @param num
	 * 			the number code corresponding to the error being created.
	 * @param mes
	 * 			the message associated with the error code.
	 * @param correctionValue
	 * 			the recommended fix for the given error code.
	 */
	public Error(int num, String mes, String correctionValue)
	{
		this.correction = correctionValue;
		this.number = num;
		this.message = mes;
	}
	/**
	 * Constructor for Error Object.
	 */
	public Error()
	{

	}




}
