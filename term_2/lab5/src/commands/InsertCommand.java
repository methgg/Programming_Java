package commands;


import java.util.Scanner;

import manager.CollectionManager;
import models.MusicBand;
import util.InputProvider;
import util.JsonUtil;
import util.ReadMusicBandFromUser;
import util.ScriptParser;

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
            String[] parts = args.split(" ", 2);
            Long key = Long.parseLong(parts[0]);

            MusicBand band;


            if (parts.length > 1) {
                band = JsonUtil.getGson().fromJson(ScriptParser.convertToJson(parts[1]), MusicBand.class);
                band.setId(key);
            } else {
                Scanner scanner = InputProvider.getScanner();
                band = new ReadMusicBandFromUser(scanner).read();
            }

            cm.insert(key, band);

            System.out.println("Элемент успешно добавлен");

        } catch (Exception e) {
            System.out.println("Ошибка команды insert: " + e.getMessage());
        }
    }

    @Override 
    public String getDescription(){
        return "добавить новый элемент с заданным ключом";
    }
   
}