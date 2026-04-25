package exceptions;

/**
 * Класс, содержащий сообщения команд
 */
public final class ErrorMessages {
    private ErrorMessages() {
    }

    public static final String UNKNOWN_COMMAND = "Команда не найдена. Введите help.";
    public static final String FILE_NAME_REQUIRED = "Укажите имя файла.";
    public static final String COLLECTION_EMPTY = "Коллекция пуста.";
    public static final String COLLECTION_CLEARED = "Коллекция очищена.";
    public static final String APP_EXIT = "Работа консольного приложения завершена.";
    public static final String AVAILABLE_COMMANDS = "Доступные команды:";
    public static final String COMPARE_REFERENCE_PROMPT = "Введите параметры элемента для сравнения:";
    public static final String NEW_ELEMENT_PROMPT = "Введите новый элемент:";
    public static final String INSERT_PARSE_ERROR = "Не удалось разобрать объект MusicBand из JSON.";
    public static final String ELEMENT_ADDED = "Элемент успешно добавлен.";
    public static final String ELEMENT_REPLACED = "Элемент заменен.";
    public static final String NEW_ELEMENT_NOT_GREATER = "Новый элемент не больше старого. Замена не выполнена.";
    public static final String REMOVE_LOWER_DONE = "Все элементы меньше заданного удалены.";
    public static final String INVALID_KEY = "Ключ должен быть целым числом.";
    public static final String INVALID_ID = "ID должен быть целым положительным числом.";
    public static final String INVALID_NUMBER = "Значение должно быть целым числом.";
    public static final String INVALID_GENRE = "Некорректный жанр.";
    public static final String INVALID_DATE = "Некорректный формат даты.";
    public static final String INVALID_EYE_COLOR = "Некорректный цвет глаз.";
    public static final String SCRIPT_RECURSION = "Обнаружена рекурсия. Скрипт уже выполняется: ";
    public static final String BAND_NAME_EMPTY = "Имя группы не может быть пустым.";
    public static final String BAND_NAME_NULL = "Имя группы не может быть null.";
    public static final String COORDINATES_NULL = "Координаты не могут быть null.";
    public static final String COORDINATE_X_NULL = "Координата X не может быть null.";
    public static final String COORDINATE_Y_NULL = "Координата Y не может быть null.";
    public static final String COORDINATE_Y_TOO_LARGE = "Координата Y не может быть больше 525.";
    public static final String PARTICIPANTS_INVALID = "Количество участников должно быть больше 0.";
    public static final String GENRE_NULL = "Жанр не может быть null.";
    public static final String FRONTMAN_NULL = "Фронтмен не может быть null.";
    public static final String PERSON_NAME_NULL = "Имя фронтмена не может быть null.";
    public static final String PERSON_NAME_EMPTY = "Имя фронтмена не может быть пустым.";
    public static final String HEIGHT_INVALID = "Рост должен быть больше 0.";
    public static final String PASSPORT_NULL = "PassportID не может быть null.";
    public static final String PASSPORT_EMPTY = "PassportID не может быть пустым.";
    public static final String INPUT_COORDINATE_X = "Координата X должна быть целым числом.";
    public static final String INPUT_COORDINATE_Y = "Координата Y должна быть числом.";
    public static final String INPUT_PARTICIPANTS = "Количество участников должно быть целым числом.";
    public static final String INPUT_HEIGHT = "Рост должен быть числом.";

    public static String commandError(String commandName, String details) {
        return "Ошибка команды " + commandName + ": " + details;
    }

    public static String scriptError(String details) {
        return "Ошибка скрипта: " + details;
    }

    public static String fileNotFound(String filename) {
        return "Файл не найден: " + filename + ". Коллекция будет пустой.";
    }

    public static String fileReadError(String details) {
        return "Ошибка при чтении файла: " + details;
    }

    public static String jsonSyntaxError(String details) {
        return "Ошибка синтаксиса JSON: " + details;
    }

    public static String fileSaveError(String details) {
        return "Ошибка при сохранении файла: " + details;
    }

    public static String elementNotFound(long key) {
        return "Элемент с ключом " + key + " не найден.";
    }

    public static String elementWithIdNotFound(long id) {
        return "Элемент с ID " + id + " не найден.";
    }

    public static String removedByKey(long key) {
        return "Элемент с ключом " + key + " удален.";
    }

    public static String updatedByKey(long key) {
        return "Элемент с ключом " + key + " обновлен.";
    }

     public static String updatedById(long id) {
        return "Элемент с ID " + id + " обновлен.";
    }

    public static String removedGreaterKeys(long key) {
        return "Элементы с ключами больше " + key + " удалены.";
    }

    public static String savedToFile(String filename) {
        return "Коллекция сохранена в " + filename + ".";
    }

    public static String countGreaterThanParticipants(int number, long count) {
        return "Количество элементов с numberOfParticipants > " + number + ": " + count;
    }

    public static String unsupportedClientCommand(String commandName) {
    return "Команда пока не поддерживается клиентом: " + commandName;
    }

    public static String missingArgument(String commandName) {
        return "Не указан аргумент команды " + commandName + ".";
    }

    public static String inputError(String details) {
    return "Ошибка ввода: " + details;
    }

    public static String invalidRequest() {
    return "Некорректный запрос.";
    }

    public static String commandExecutionError(String details) {
        return "Ошибка выполнения команды: " + details;
    }

    public static String scannerNotInitialized() {
        return "Сканер не установлен. Сначала вызовите setScanner.";
    }

    public static String inputReadError(String details) {
        return "Ошибка чтения ввода: " + details;
    }

    public static String terminalSetupError(String details) {
        return "Ошибка настройки терминала: " + details;
    }
    public static String serverUnavailable(String details) {
        return "Сервер временно недоступен: " + details;
    }


}
