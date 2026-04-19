package util;

import java.time.LocalDate;

import exceptions.ErrorMessages;
import models.Color;
import models.Coordinates;
import models.MusicBand;
import models.MusicGenre;
import models.Person;

/**
 * Класс, читающий все значения для ключей модели MusicBand от пользователя
 */
public class ReadMusicBandFromUser{

    public MusicBand read() {
        String name;
        while (true) {
            name = InputProvider.readLine("Введите имя группы: ");
            if (name != null && !name.trim().isEmpty()) {
                break;
            }
            System.out.println(ErrorMessages.BAND_NAME_EMPTY);
        }

        Integer x;
        while (true) {
            try {
                x = Integer.parseInt(InputProvider.readLine("Введите координату X: "));
                break;
            } catch (NumberFormatException e) {
                System.out.println(ErrorMessages.INPUT_COORDINATE_X);
            }
        }

        Double y;
        while (true) {
            try {
                y = Double.parseDouble(InputProvider.readLine("Введите координату Y (<=525): "));
                if (y <= 525) break;
                else System.out.println(ErrorMessages.COORDINATE_Y_TOO_LARGE);
            } catch (NumberFormatException e) {
                System.out.println(ErrorMessages.INPUT_COORDINATE_Y);
            }
        }

        Coordinates coordinates = new Coordinates(x, y);

        Integer participants;
        while (true) {
            try {
                participants = Integer.parseInt(InputProvider.readLine("Введите количество участников (>0): "));
                if (participants > 0) break;
                else System.out.println(ErrorMessages.PARTICIPANTS_INVALID);
            } catch (NumberFormatException e) {
                System.out.println(ErrorMessages.INPUT_PARTICIPANTS);
            }
        }
        
        MusicGenre genre;
        while (true) {
            System.out.println("Доступные жанры:");
            for (MusicGenre g : MusicGenre.values()) {
                System.out.println(g);
            }
            try {
                genre = MusicGenre.valueOf(InputProvider.readLine("Введите жанр: ").toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(ErrorMessages.INVALID_GENRE);
            }
        }

        String frontName;
        while (true) {
            frontName = InputProvider.readLine("Введите имя фронтмена: ");
            if (frontName != null && !frontName.trim().isEmpty()) break;
            System.out.println(ErrorMessages.PERSON_NAME_EMPTY);
        }

        LocalDate birthday = null;
        while (true) {
            String birth = InputProvider.readLine("Введите дату рождения (yyyy-mm-dd) или пустую строку: ");
            if (birth.isEmpty()) {
                break;
            }
            try {
                birthday = LocalDate.parse(birth);
                break;
            } catch (Exception e) {
                System.out.println(ErrorMessages.INVALID_DATE);
            }
        }
        
        Long height = null;
        while (true) {
            String heightStr = InputProvider.readLine("Введите рост или пустую строку: ");
            if (heightStr.isEmpty()) break;
            try {
                height = Long.parseLong(heightStr);
                if (height > 0) break;
                else System.out.println(ErrorMessages.HEIGHT_INVALID);
            } catch (NumberFormatException e) {
                System.out.println(ErrorMessages.INPUT_HEIGHT);
            }
        }

        String passport;
        while (true) {
            passport = InputProvider.readLine("Введите passportID: ");
            if (passport != null && !passport.trim().isEmpty()) break;
            System.out.println(ErrorMessages.PASSPORT_EMPTY);
        }

        Color eyeColor = null;
        while (true) {
            System.out.println("Цвет глаз (BLACK, YELLOW, ORANGE) или пустая строка:");
            String colorStr = InputProvider.readLine("");
            if (colorStr.isEmpty()) break;
            try {
                eyeColor = Color.valueOf(colorStr.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(ErrorMessages.INVALID_EYE_COLOR);
            }
        }

        Person frontMan = new Person(frontName, birthday, height, passport, eyeColor);

        return new MusicBand(name, coordinates, participants, genre, frontMan);
    }
}
