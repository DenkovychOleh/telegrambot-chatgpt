package telegrambotchatgpt.exceptions;

public class AuthorizationCustomException extends RuntimeException {
    public AuthorizationCustomException(String message) {
        super(message);
    }

    public AuthorizationCustomException() {}
}
