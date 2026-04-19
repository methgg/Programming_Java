package commands;

import java.util.ArrayList;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import util.ReadMusicBandFromUser;


/**
 * Класс, который реализует команду, которая обновляет значение элемента по ID
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
            long id = Long.parseLong(args.trim());
            ArrayList<Long> listV = new ArrayList<>();
            ArrayList<Long> listK = new ArrayList<>();
            cm.getCollection().forEach((k, v) -> listV.add(v.getId()));
            cm.getCollection().forEach((k, v) -> listK.add(k));
            if (!listV.contains(id)) {
                System.out.println(ErrorMessages.elementWithIdNotFound(id));
                return;
            }
            ReadMusicBandFromUser reader = new ReadMusicBandFromUser();
            MusicBand newBand = reader.read();
            newBand.setId(id);
            cm.getCollection().put(listK.get(listV.indexOf(id)), newBand);
            System.out.println(ErrorMessages.updatedById(id));

        } catch (NumberFormatException e) {
            System.out.println(ErrorMessages.commandError("update", ErrorMessages.INVALID_ID));
        } catch (Exception e) {
            System.out.println(ErrorMessages.commandError("update", e.getMessage()));
        }
    }
    @Override 
    public String getDescription(){
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
