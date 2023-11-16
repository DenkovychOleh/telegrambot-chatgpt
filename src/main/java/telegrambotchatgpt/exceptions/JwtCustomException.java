package telegrambotchatgpt.exceptions;

public class JwtCustomException extends RuntimeException {
    public JwtCustomException(String message) {
        super(message);
    }
}