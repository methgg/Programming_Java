package commands;

import java.util.Scanner;

import manager.CollectionManager;
import models.MusicBand;
import util.InputProvider;
import util.ReadMusicBandFromUser;

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
            ReadMusicBandFromUser reader = new ReadMusicBandFromUser(scanner);
            MusicBand newBand = reader.read();
            cm.getCollection().put(key, newBand);
            System.out.println("Элемент с ключом " + key + " обновлён.");

        } catch (Exception e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        }
    }
    @Override 
    public String getDescription(){
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}