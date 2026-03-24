package commands;

import java.util.Scanner;

import manager.CollectionManager;
import models.MusicBand;
import util.InputProvider;
import util.ReadMusicBandFromUser;

public class ReplaceIfGreaterCommand implements Command {

    private CollectionManager cm;
    private String args;

    public ReplaceIfGreaterCommand(CollectionManager cm) {
        this.cm = cm;
    }

    @Override
    public void setArgs(String args) {
        this.args = args;
    }

    @Override
    public void execute() {

        try {
            Long key = Long.parseLong(args.trim());

            if (!cm.getCollection().containsKey(key)) {
                System.out.println("Элемент с таким ключом не найден");
                return;
            }

            Scanner scanner = InputProvider.getScanner();

            System.out.println("Введите новый элемент:");

            ReadMusicBandFromUser reader = new ReadMusicBandFromUser(scanner);
            MusicBand newBand = reader.read();

            MusicBand oldBand = cm.getCollection().get(key);

            if (newBand.compareTo(oldBand) > 0) {
                cm.getCollection().put(key, newBand);
                System.out.println("Элемент заменён");
            } else {
                System.out.println("Новый элемент не больше старого. Замена не выполнена.");
            }

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}