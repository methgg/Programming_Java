package util;

/**
 * Класс для генерации ID 
 */
public class IdGenerator {
    private static long currentId = 1;

    public static Long generateId() {
        return currentId++;
    }
    public static void updateCurrentId(long maxId){
        currentId = maxId + 1;
    }
}