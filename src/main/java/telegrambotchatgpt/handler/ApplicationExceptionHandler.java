package telegrambotchatgpt.handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import telegrambotchatgpt.exceptions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return createValidationErrorResponse(exception.getBindingResult().getFieldErrors());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataNotFoundException.class)
    public Map<String, String> handleDataNotFoundException(DataNotFoundException exception) {
        return createErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InsufficientPermissionsException.class)
    public Map<String, String> handleInsufficientPermissionsException(InsufficientPermissionsException exception) {
        return createErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(JwtCustomException.class)
    public Map<String, String> handleJwtCustomException(JwtCustomException exception) {
        return createErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationCustomException.class)
    public Map<String, String> handleAuthorizationCustomException(AuthorizationCustomException exception) {
        return createErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidValueException.class)
    public Map<String, String> handleInvalidValueException(InvalidValueException exception) {
        return createErrorResponse(exception.getMessage());
    }


    private Map<String, String> createErrorResponse(String errorMessage) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", errorMessage);
        return errorMap;
    }

    private Map<String, String> createValidationErrorResponse(List<FieldError> fieldErrors) {
        Map<String, String> errorMap = new HashMap<>();
        fieldErrors.forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }
}
