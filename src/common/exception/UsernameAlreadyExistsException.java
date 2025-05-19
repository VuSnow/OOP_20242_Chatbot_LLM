package common.exception;

public class UsernameAlreadyExistsException extends AimsException{
	public UsernameAlreadyExistsException() {
        super("Username already exists.");
    }
}
