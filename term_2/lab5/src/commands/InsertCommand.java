package commands;


import java.util.Scanner;

import exceptions.CommandException;
import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import util.InputProvider;
import util.JsonUtil;
import util.ReadMusicBandFromUser;

/**
 * Класс, который реализует команду, которая добавляет новый элемент в коллекцию с заданным ключом
 */
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
                band = JsonUtil.getGson().fromJson(parts[1], MusicBand.class);
                if (band == null) {
                    throw new CommandException(ErrorMessages.INSERT_PARSE_ERROR);
                }
                band.setId(key);
            } else {
                Scanner scanner = InputProvider.getScanner();
                band = new ReadMusicBandFromUser(scanner).read();
            }

            cm.insert(key, band);

            System.out.println(ErrorMessages.ELEMENT_ADDED);

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println(ErrorMessages.commandError("insert", ErrorMessages.INVALID_KEY));
        } catch (Exception e) {
            System.out.println(ErrorMessages.commandError("insert", e.getMessage()));
        }
    }

    @Override 
    public String getDescription(){
        return "добавить новый элемент с заданным ключом";
    }
   
}
