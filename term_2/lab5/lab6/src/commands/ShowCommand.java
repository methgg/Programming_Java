package commands;

import manager.CollectionManager;



/**
 * Класс, который реализует команду, которая выводит коллекцию
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
    @Override 
    public String getDescription(){
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}