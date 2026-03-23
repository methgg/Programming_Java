package util;

/**
 * Генератор уникальных id для элементов коллекции.
 */
public class IdGenerator {
    private static long currentId = 1;

    public static Long generateId() {
        return currentId++;
    }
}