package commands;

import exceptions.ErrorMessages;
import manager.CollectionManager;

public class RemoveKeyCommand implements Command {
    private CollectionManager cm;
    private String args;

    public RemoveKeyCommand(CollectionManager cm) { this.cm = cm; }
    @Override public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        try {
            long key = Long.parseLong(args.trim());
            cm.remove(key);
            System.out.println(ErrorMessages.removedByKey(key));
        } catch (NumberFormatException e) {
            System.out.println(ErrorMessages.commandError("remove_key", ErrorMessages.INVALID_KEY));
        }
    }
    @Override 
    public String getDescription(){
        return "удалить элемент из коллекции по его ключу";
    }
}
