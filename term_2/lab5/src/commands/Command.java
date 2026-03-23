package commands;

/**
 * Контракт команды консольного приложения.
 * Команда должна уметь принять аргументы и выполнить своё действие.
 */
public interface Command {
    void setArgs(String args);
    void execute();
}