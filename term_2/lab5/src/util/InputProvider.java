package util;

import java.util.Scanner;

/**
 * Класс для установки сканнера
 *
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
            throw new IllegalStateException("Scanner не установлен. Вызывайте setScanner перед execute().");
        }
        return sc;
    }

    public static void clear() {
        CURRENT.remove();
    }
}
