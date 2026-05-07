package exceptions;

/**
 * Класс для ошибок ввода команд
 */
public class CommandException extends RuntimeException {
    public CommandException(String message) {
        super(message);
    }
}
