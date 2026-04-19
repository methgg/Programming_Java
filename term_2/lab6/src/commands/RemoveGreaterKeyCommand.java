package commands;

import java.util.Iterator;
import java.util.Map;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;


/**
 * Класс, который удаляет из коллекции все элементы, ключ которых превышает заданный
 */
public class RemoveGreaterKeyCommand implements Command {
    private CollectionManager cm;
    private String args;

    public RemoveGreaterKeyCommand(CollectionManager cm) { this.cm = cm; }
    @Override
    public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        try {
            long key = Long.parseLong(args.trim());
            Iterator<Map.Entry<Long, MusicBand>> it = cm.getCollection().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Long, MusicBand> entry = it.next();
                if (entry.getKey() > key) it.remove();
            }
            System.out.println(ErrorMessages.removedGreaterKeys(key));
        } catch (NumberFormatException e) {
            System.out.println(ErrorMessages.commandError("remove_greater_key", ErrorMessages.INVALID_KEY));
        }
    }
    @Override 
    public String getDescription(){
        return "удалить из коллекции все элементы, ключ которых превышает заданный";
    }
}
