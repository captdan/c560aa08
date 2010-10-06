public class ErrorContainer implements ErrorInterface {
	/**
	 * A slightly different implementation of Error that only keeps the first three errors.
	 * we can also add a boolean function to check if we reach 3 error and terminate execution.
	 * 
	 */
	public int[] number = new int[3];
	public String[] message = new String[3];
	public int counter = 0;

	public void CreateError(int num, String message) {
		if (counter < 3) {
			this.number[counter] = num;
			this.message[counter] = message;
		this.counter++;
		}
	}
}
