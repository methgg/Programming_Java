package commands;


import java.util.Scanner;

import manager.CollectionManager;
import models.MusicBand;
import util.InputProvider;
import util.ReadMusicBandFromUser;

public class InsertCommand implements Command {

    private CollectionManager cm;
    private String args;

    public InsertCommand(CollectionManager cm) {
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
            Scanner scanner = InputProvider.getScanner();

            ReadMusicBandFromUser reader = new ReadMusicBandFromUser(scanner);
            MusicBand band = reader.read();

            cm.insert(key, band);

            System.out.println("Элемент успешно добавлен");

        } catch (Exception e) { 
            System.out.println("Ошибка ввода: " + e.getMessage());
        }
    }

   
}