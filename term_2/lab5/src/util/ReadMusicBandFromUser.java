package util;

import java.time.LocalDate;
import java.util.Scanner;

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
    private final Scanner scanner;

    public ReadMusicBandFromUser(Scanner scanner) {
        this.scanner = scanner;
    }

    public MusicBand read() {
        String name;
        while (true) {
            System.out.print("Введите имя группы: ");
            name = scanner.nextLine();
            if (name != null && !name.trim().isEmpty()) {
                break;
            }
            System.out.println(ErrorMessages.BAND_NAME_EMPTY);
        }

        Integer x;
        while (true) {
            System.out.print("Введите координату X: ");
            try {
                x = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println(ErrorMessages.INPUT_COORDINATE_X);
            }
        }

        Double y;
        while (true) {
            System.out.print("Введите координату Y (<=525): ");
            try {
                y = Double.parseDouble(scanner.nextLine());
                if (y <= 525) break;
                else System.out.println(ErrorMessages.COORDINATE_Y_TOO_LARGE);
            } catch (NumberFormatException e) {
                System.out.println(ErrorMessages.INPUT_COORDINATE_Y);
            }
        }

        Coordinates coordinates = new Coordinates(x, y);

        Integer participants;
        while (true) {
            System.out.print("Введите количество участников (>0): ");
            try {
                participants = Integer.parseInt(scanner.nextLine());
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
            System.out.print("Введите жанр: ");
            try {
                genre = MusicGenre.valueOf(scanner.nextLine().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(ErrorMessages.INVALID_GENRE);
            }
        }

        String frontName;
        while (true) {
            System.out.print("Введите имя фронтмена: ");
            frontName = scanner.nextLine();
            if (frontName != null && !frontName.trim().isEmpty()) break;
            System.out.println(ErrorMessages.PERSON_NAME_EMPTY);
        }

        LocalDate birthday = null;
        while (true) {
            System.out.print("Введите дату рождения (yyyy-mm-dd) или пустую строку: ");
            String birth = scanner.nextLine();
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
            System.out.print("Введите рост или пустую строку: ");
            String heightStr = scanner.nextLine();
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
            System.out.print("Введите passportID: ");
            passport = scanner.nextLine();
            if (passport != null && !passport.trim().isEmpty()) break;
            System.out.println(ErrorMessages.PASSPORT_EMPTY);
        }

        Color eyeColor = null;
        while (true) {
            System.out.println("Цвет глаз (BLACK, YELLOW, ORANGE) или пустая строка:");
            String colorStr = scanner.nextLine();
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
