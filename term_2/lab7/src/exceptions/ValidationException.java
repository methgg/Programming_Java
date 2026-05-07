package exceptions;

/**
 * Класс для ошибок валидности данных
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
