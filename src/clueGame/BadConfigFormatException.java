package clueGame;

public class BadConfigFormatException extends Exception  {
	
	/**
	 * java made me do it
	 */
	private static final long serialVersionUID = 1L;

	// Constructor with message passed in
	public BadConfigFormatException(String message) {
		super(message);
	}
	
	public BadConfigFormatException() {
		super("Bad config file format.");
	}
}
