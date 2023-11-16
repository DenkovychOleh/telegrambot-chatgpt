package telegrambotchatgpt.exceptions;

public class InsufficientPermissionsException extends RuntimeException {
    public InsufficientPermissionsException(String message) {
        super("Insufficient permissions: " + message);
    }
}