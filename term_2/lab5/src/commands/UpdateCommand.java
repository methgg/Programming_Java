package commands;

import manager.CollectionManager;
import models.*;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Обновляет элемент коллекции по ключу
 */
public class UpdateCommand implements Command {
    private CollectionManager cm;
    private String args;

    public UpdateCommand(CollectionManager cm) { this.cm = cm; }

    @Override
    public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        try {
            long key = Long.parseLong(args.trim());
            if (!cm.getCollection().containsKey(key)) {
                System.out.println("Элемент с ключом " + key + " не найден.");
                return;
            }
            Scanner scanner = InputProvider.getScanner();
            System.out.print("Имя группы: "); String name = scanner.nextLine();
            System.out.print("X: "); int x = Integer.parseInt(scanner.nextLine());
            System.out.print("Y (<=525): "); double y = Double.parseDouble(scanner.nextLine());
            Coordinates coordinates = new Coordinates(x, y);

            System.out.print("Количество участников (>0): "); int participants = Integer.parseInt(scanner.nextLine());

            System.out.println("Жанры: RAP, SOUL, BLUES, MATH_ROCK, PUNK_ROCK");
            MusicGenre genre = MusicGenre.valueOf(scanner.nextLine().toUpperCase());

            System.out.print("Имя фронтмена: "); String frontName = scanner.nextLine();
            System.out.print("День рождения (yyyy-MM-dd) или пустая строка: "); String birth = scanner.nextLine();
            LocalDate birthday = birth.isEmpty() ? null : LocalDate.parse(birth);

            System.out.print("Рост (или пустая строка): "); String hStr = scanner.nextLine();
            Long height = hStr.isEmpty() ? null : Long.parseLong(hStr);

            System.out.print("PassportID: "); String passport = scanner.nextLine();

            System.out.println("Цвет глаз: BLACK, YELLOW, ORANGE или пустая строка");
            String colorStr = scanner.nextLine();
            Color eyeColor = colorStr.isEmpty() ? null : Color.valueOf(colorStr.toUpperCase());

            Person frontMan = new Person(frontName, birthday, height, passport, eyeColor);
            MusicBand newBand = new MusicBand(name, coordinates, participants, genre, frontMan);
            cm.getCollection().put(key, newBand);
            System.out.println("Элемент с ключом " + key + " обновлён.");

        } catch (Exception e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        }
    }
}