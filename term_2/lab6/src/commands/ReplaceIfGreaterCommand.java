package commands;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import util.ReadMusicBandFromUser;



/**
 * Класс, который реализует команду, которая заменяет значение по ключу, если новое значение больше старого
 */
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
                System.out.println(ErrorMessages.elementNotFound(key));
                return;
            }

            System.out.println(ErrorMessages.NEW_ELEMENT_PROMPT);

            ReadMusicBandFromUser reader = new ReadMusicBandFromUser();
            MusicBand newBand = reader.read();

            MusicBand oldBand = cm.getCollection().get(key);

            if (newBand.compareTo(oldBand) > 0) {
                cm.getCollection().put(key, newBand);
                System.out.println(ErrorMessages.ELEMENT_REPLACED);
            } else {
                System.out.println(ErrorMessages.NEW_ELEMENT_NOT_GREATER);
            }

        } catch (NumberFormatException e) {
            System.out.println(ErrorMessages.commandError("replace_if_greater", ErrorMessages.INVALID_KEY));
        } catch (Exception e) {
            System.out.println(ErrorMessages.commandError("replace_if_greater", e.getMessage()));
        }
    }
    @Override 
    public String getDescription(){
        return "заменить значение по ключу, если новое значение больше старого";
    }
}
