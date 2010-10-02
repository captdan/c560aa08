/**
 * 
 * @author C560aa08 Just one of the implementation for the Error class
 */
public class Error implements ErrorInterface {

	public int number;
	public String message;

	/**
	 * @param num
	 *            : integer based number associated with an error.
	 * @param mes
	 *            : String containing the proper description for an error event.
	 */
	// creates a single instance of an error
	public final void CreateError(final int num, String mes) {
		this.number = num;
		this.message = mes;

	}

}
