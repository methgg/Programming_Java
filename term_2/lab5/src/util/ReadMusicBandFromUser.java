package util;

import java.time.LocalDate;
import java.util.Scanner;

import models.Color;
import models.Coordinates;
import models.MusicBand;
import models.MusicGenre;
import models.Person;


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
            System.out.println("Имя не может быть пустым.");
        }

        Integer x;
        while (true) {
            System.out.print("Введите координату X: ");
            try {
                x = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Координата X должна быть целым числом.");
            }
        }

        Double y;
        while (true) {
            System.out.print("Введите координату Y (<=525): ");
            try {
                y = Double.parseDouble(scanner.nextLine());
                if (y <= 525) break;
                else System.out.println("Координата Y не может быть больше 525.");
            } catch (NumberFormatException e) {
                System.out.println("Координата Y должна быть числом.");
            }
        }

        Coordinates coordinates = new Coordinates(x, y);

        Integer participants;
        while (true) {
            System.out.print("Введите количество участников (>0): ");
            try {
                participants = Integer.parseInt(scanner.nextLine());
                if (participants > 0) break;
                else System.out.println("Количество участников должно быть больше 0.");
            } catch (NumberFormatException e) {
                System.out.println("Количество участников должно быть целым числом.");
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
                System.out.println("Некорректный жанр. Попробуйте снова.");
            }
        }

        String frontName;
        while (true) {
            System.out.print("Введите имя фронтмена: ");
            frontName = scanner.nextLine();
            if (frontName != null && !frontName.trim().isEmpty()) break;
            System.out.println("Имя фронтмена не может быть пустым.");
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
                System.out.println("Некорректный формат даты.");
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
                else System.out.println("Рост должен быть больше 0.");
            } catch (NumberFormatException e) {
                System.out.println("Рост должен быть числом.");
            }
        }

        String passport;
        while (true) {
            System.out.print("Введите passportID: ");
            passport = scanner.nextLine();
            if (passport != null && !passport.trim().isEmpty()) break;
            System.out.println("PassportID не может быть пустым.");
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
                System.out.println("Некорректный цвет глаз.");
            }
        }

        Person frontMan = new Person(frontName, birthday, height, passport, eyeColor);

        return new MusicBand(name, coordinates, participants, genre, frontMan);
    }
}
