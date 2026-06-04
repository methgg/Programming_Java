package gui;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class GuiResources {
    public static final Locale RU = new Locale("ru", "RU");
    public static final Locale NO = new Locale("no", "NO");
    public static final Locale DA = new Locale("da", "DK");
    public static final Locale EN_IN = new Locale("en", "IN");

    private static final Map<Locale, Map<String, String>> TEXTS = new HashMap<>();

    static {
        TEXTS.put(RU, mapOf(new String[][] {
                {"app.title", "Галерея музыкальных групп"},
                {"user", "Пользователь"},
                {"language", "Язык"},
                {"commands", "команды"},
                {"filter", "Фильтр"},
                {"column", "Колонка"},
                {"sort", "Сортировка"},
                {"order", "Порядок"},
                {"asc", "Возр."},
                {"desc", "Убыв."},
                {"table", "ТАБЛИЦА"},
                {"visualization", "ВИЗУАЛИЗАЦИЯ"},
                {"status", "Статус"},
                {"loading", "Загрузка коллекции..."},
                {"loaded", "Загружено объектов: {0}"},
                {"selected.object", "Выбранный объект:"},
                {"key", "Ключ"},
                {"owner", "Владелец"},
                {"column.key", "Ключ"},
                {"column.id", "ID"},
                {"column.name", "Название"},
                {"column.x", "X"},
                {"column.y", "Y"},
                {"column.creationDate", "Дата создания"},
                {"column.participants", "Участники"},
                {"column.genre", "Жанр"},
                {"column.frontMan", "Фронтмен"},
                {"column.birthday", "Дата рождения"},
                {"column.height", "Рост"},
                {"column.passport", "Паспорт"},
                {"column.eyeColor", "Цвет глаз"},
                {"auth.title", "Авторизация"},
                {"login", "Логин"},
                {"password", "Пароль"},
                {"register", "Регистрация"},
                {"login.empty", "Логин пуст."},
                {"password.empty", "Пароль пуст."},
                {"connecting", "Подключение..."},
                {"connection.error", "Ошибка соединения: {0}"},
                {"ok", "OK"},
                {"cancel", "Отмена"},
                {"input.error", "Ошибка ввода"},
                {"field.name", "Название"},
                {"field.x", "X"},
                {"field.y", "Y"},
                {"field.participants", "Участники"},
                {"field.genre", "Жанр"},
                {"field.frontManName", "Имя фронтмена"},
                {"field.birthday", "Дата рождения (yyyy-mm-dd)"},
                {"field.height", "Рост"},
                {"field.passport", "Паспорт"},
                {"field.eyeColor", "Цвет глаз"}
        }));

        TEXTS.put(NO, mapOf(new String[][] {
                {"app.title", "MusicBand-galleri"},
                {"user", "Bruker"},
                {"language", "Sprak"},
                {"commands", "kommandoer"},
                {"filter", "Filter"},
                {"column", "Kolonne"},
                {"sort", "Sortering"},
                {"order", "Rekkefolge"},
                {"asc", "Stig."},
                {"desc", "Synk."},
                {"table", "TABELL"},
                {"visualization", "VISUALISERING"},
                {"status", "Status"},
                {"loading", "Laster samling..."},
                {"loaded", "Lastet objekter: {0}"},
                {"selected.object", "Valgt objekt:"},
                {"key", "Nokkel"},
                {"owner", "Eier"},
                {"column.key", "Nokkel"},
                {"column.id", "ID"},
                {"column.name", "Navn"},
                {"column.x", "X"},
                {"column.y", "Y"},
                {"column.creationDate", "Opprettet"},
                {"column.participants", "Deltakere"},
                {"column.genre", "Sjanger"},
                {"column.frontMan", "Frontfigur"},
                {"column.birthday", "Fodselsdag"},
                {"column.height", "Hoyde"},
                {"column.passport", "Pass"},
                {"column.eyeColor", "Oyefarge"},
                {"auth.title", "Autorisasjon"},
                {"login", "Innlogging"},
                {"password", "Passord"},
                {"register", "Registrer"},
                {"login.empty", "Innlogging er tom."},
                {"password.empty", "Passord er tomt."},
                {"connecting", "Kobler til..."},
                {"connection.error", "Tilkoblingsfeil: {0}"},
                {"ok", "OK"},
                {"cancel", "Avbryt"},
                {"input.error", "Inndatafeil"},
                {"field.name", "Navn"},
                {"field.x", "X"},
                {"field.y", "Y"},
                {"field.participants", "Deltakere"},
                {"field.genre", "Sjanger"},
                {"field.frontManName", "Frontfigur-navn"},
                {"field.birthday", "Fodselsdag (yyyy-mm-dd)"},
                {"field.height", "Hoyde"},
                {"field.passport", "Pass"},
                {"field.eyeColor", "Oyefarge"}
        }));

        TEXTS.put(DA, mapOf(new String[][] {
                {"app.title", "MusicBand-galleri"},
                {"user", "Bruger"},
                {"language", "Sprog"},
                {"commands", "kommandoer"},
                {"filter", "Filter"},
                {"column", "Kolonne"},
                {"sort", "Sortering"},
                {"order", "Raekkefolge"},
                {"asc", "Stig."},
                {"desc", "Fald."},
                {"table", "TABEL"},
                {"visualization", "VISUALISERING"},
                {"status", "Status"},
                {"loading", "Indlaeser samling..."},
                {"loaded", "Indlaeste objekter: {0}"},
                {"selected.object", "Valgt objekt:"},
                {"key", "Nogle"},
                {"owner", "Ejer"},
                {"column.key", "Nogle"},
                {"column.id", "ID"},
                {"column.name", "Navn"},
                {"column.x", "X"},
                {"column.y", "Y"},
                {"column.creationDate", "Oprettet"},
                {"column.participants", "Deltagere"},
                {"column.genre", "Genre"},
                {"column.frontMan", "Frontfigur"},
                {"column.birthday", "Fodselsdag"},
                {"column.height", "Hojde"},
                {"column.passport", "Pas"},
                {"column.eyeColor", "Ojenfarve"},
                {"auth.title", "Autorisation"},
                {"login", "Login"},
                {"password", "Adgangskode"},
                {"register", "Registrer"},
                {"login.empty", "Login er tomt."},
                {"password.empty", "Adgangskode er tom."},
                {"connecting", "Forbinder..."},
                {"connection.error", "Forbindelsesfejl: {0}"},
                {"ok", "OK"},
                {"cancel", "Annuller"},
                {"input.error", "Inputfejl"},
                {"field.name", "Navn"},
                {"field.x", "X"},
                {"field.y", "Y"},
                {"field.participants", "Deltagere"},
                {"field.genre", "Genre"},
                {"field.frontManName", "Frontfigur-navn"},
                {"field.birthday", "Fodselsdag (yyyy-mm-dd)"},
                {"field.height", "Hojde"},
                {"field.passport", "Pas"},
                {"field.eyeColor", "Ojenfarve"}
        }));

        TEXTS.put(EN_IN, mapOf(new String[][] {
                {"app.title", "MusicBand Gallery"},
                {"user", "User"},
                {"language", "Language"},
                {"commands", "commands"},
                {"filter", "Filter"},
                {"column", "Column"},
                {"sort", "Sort"},
                {"order", "Order"},
                {"asc", "Asc"},
                {"desc", "Desc"},
                {"table", "TABLE"},
                {"visualization", "VISUALIZATION"},
                {"status", "Status"},
                {"loading", "Loading collection..."},
                {"loaded", "Loaded objects: {0}"},
                {"selected.object", "Selected object:"},
                {"key", "Key"},
                {"owner", "Owner"},
                {"column.key", "Key"},
                {"column.id", "ID"},
                {"column.name", "Name"},
                {"column.x", "X"},
                {"column.y", "Y"},
                {"column.creationDate", "Creation Date"},
                {"column.participants", "Participants"},
                {"column.genre", "Genre"},
                {"column.frontMan", "Front Man"},
                {"column.birthday", "Birthday"},
                {"column.height", "Height"},
                {"column.passport", "Passport"},
                {"column.eyeColor", "Eye Color"},
                {"auth.title", "Authorization"},
                {"login", "Login"},
                {"password", "Password"},
                {"register", "Register"},
                {"login.empty", "Login is empty."},
                {"password.empty", "Password is empty."},
                {"connecting", "Connecting..."},
                {"connection.error", "Connection error: {0}"},
                {"ok", "OK"},
                {"cancel", "Cancel"},
                {"input.error", "Input error"},
                {"field.name", "Name"},
                {"field.x", "X"},
                {"field.y", "Y"},
                {"field.participants", "Participants"},
                {"field.genre", "Genre"},
                {"field.frontManName", "Front man name"},
                {"field.birthday", "Birthday (yyyy-mm-dd)"},
                {"field.height", "Height"},
                {"field.passport", "Passport"},
                {"field.eyeColor", "Eye color"}
        }));
    }

    private GuiResources() {
    }

    public static String get(Locale locale, String key) {
        return TEXTS.getOrDefault(locale, TEXTS.get(EN_IN)).getOrDefault(
                key,
                TEXTS.get(EN_IN).getOrDefault(key, key)
        );
    }

    public static Locale localeForLabel(String label) {
        return switch (label) {
            case "RU" -> RU;
            case "NO" -> NO;
            case "DA" -> DA;
            case "EN_IN" -> EN_IN;
            default -> EN_IN;
        };
    }

    public static String labelForLocale(Locale locale) {
        if (RU.equals(locale)) {
            return "RU";
        }
        if (NO.equals(locale)) {
            return "NO";
        }
        if (DA.equals(locale)) {
            return "DA";
        }
        return "EN_IN";
    }

    private static Map<String, String> mapOf(String[][] pairs) {
        Map<String, String> map = new HashMap<>();
        for (String[] pair : pairs) {
            map.put(pair[0], pair[1]);
        }
        return map;
    }
}
