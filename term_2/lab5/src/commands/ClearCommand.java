package commands;

import manager.CollectionManager;

/**
 * Очищает коллекцию.
 */
public class ClearCommand implements Command {
    private CollectionManager cm;
    private String args;
    public ClearCommand(CollectionManager cm) { this.cm = cm; }
    @Override public void setArgs(String args) { this.args = args; }
    @Override public void execute() {
        cm.clear();
        System.out.println("Коллекция очищена.");
    }
}