package common.exception;

public class PasswordMismatchException extends AimsException{
	public PasswordMismatchException() {
		super("Passwords do not match");
	}
}
