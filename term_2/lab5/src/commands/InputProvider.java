package commands;

import java.util.Scanner;

/**
 * Общий источник {@link Scanner} для команд.
 * Используется, чтобы команды могли читать данные либо из консоли, либо из файла сценария.
 */
public final class InputProvider {
    private static final ThreadLocal<Scanner> CURRENT = new ThreadLocal<>();

    private InputProvider() {
    }

    public static void setScanner(Scanner scanner) {
        CURRENT.set(scanner);
    }

    public static Scanner getScanner() {
        Scanner sc = CURRENT.get();
        if (sc == null) {
            // На практике это должно быть установлено Invoker/ExecuteScriptCommand перед вызовом команды.
            throw new IllegalStateException("Scanner не установлен. Вызывайте setScanner перед execute().");
        }
        return sc;
    }

    public static void clear() {
        CURRENT.remove();
    }
}

