/**
 * 
 * @author C560aa08 Just one of the implementation for the Error class
 */
public class Error
{

	public int number;
	public String message;
	public String correction;

	/**
	 * @param num
	 *            : integer based number associated with an error.
	 * @param mes
	 *            : String containing the proper description for an error event.
	 */
	// creates a single instance of an error
	public final void CreateError(final int num, String mes, String correctionValue) 
	{
		this.correction = correctionValue;
		this.number = num;
		this.message = mes;

	}
	
	public final void printError(){
		System.out.println("Error number " + this.number + ":\t" );
		System.out.println(this.message);
		System.out.println(this.correction);
		
	}

}
