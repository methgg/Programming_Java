package commands;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import manager.CollectionManager;
import models.MusicBand;
import util.InputProvider;
import util.ReadMusicBandFromUser;


public class RemoveLowerCommand implements Command {
    private CollectionManager cm;
    private String args;

    public RemoveLowerCommand(CollectionManager cm) {
        this.cm = cm;
    }

    @Override
    public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        Scanner scanner = InputProvider.getScanner();
        System.out.println("Введите параметры элемента для сравнения:");
        ReadMusicBandFromUser reader = new ReadMusicBandFromUser(scanner);
        MusicBand referenceBand = reader.read();
        Iterator<Map.Entry<Long, MusicBand>> it = cm.getCollection().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, MusicBand> entry = it.next();
            if (entry.getValue().compareTo(referenceBand) < 0) it.remove();
        }
        System.out.println("Все элементы меньше заданного удалены.");
    }
    @Override 
    public String getDescription(){
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
}