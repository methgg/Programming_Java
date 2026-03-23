package commands;

import manager.CollectionManager;

/**
 * Выводит содержимое коллекции в стандартный поток вывода.
 */
public class ShowCommand implements Command {
    private CollectionManager cm;
    private String args;

    public ShowCommand(CollectionManager cm) {
        this.cm = cm; 
    }
    @Override 
    public void setArgs(String args) { 
        this.args = args;
    }
    @Override 
    public void execute() { 
        cm.show();
    }
}