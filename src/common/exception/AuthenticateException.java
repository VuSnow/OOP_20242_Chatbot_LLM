package common.exception;

public class AuthenticateException extends AimsException{
	public AuthenticateException() {
		super("Username or Password is invalid. Try again!");
	}
}
