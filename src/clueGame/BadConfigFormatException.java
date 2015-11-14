package clueGame;

public class BadConfigFormatException extends Exception  {
	// Constructor with message passed in
	public BadConfigFormatException(String message) {
		super(message);
	}
	
	public BadConfigFormatException() {
		super("Bad config file format.");
	}
}
