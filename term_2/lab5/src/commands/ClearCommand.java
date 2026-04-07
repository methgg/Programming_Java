package commands;

import exceptions.ErrorMessages;
import manager.CollectionManager;

public class ClearCommand implements Command {
    private CollectionManager cm;
    private String args;
    public ClearCommand(CollectionManager cm) { this.cm = cm; }
    @Override public void setArgs(String args) { this.args = args; }
    @Override public void execute() {
        cm.clear();
        System.out.println(ErrorMessages.COLLECTION_CLEARED);
    }
    @Override 
    public String getDescription(){
        return "очистить коллекцию";
    }
}
