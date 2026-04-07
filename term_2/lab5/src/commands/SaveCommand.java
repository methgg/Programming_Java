package commands;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import util.JsonUtil;



/**
 * Класс, который реализует команду, которая сохранят коллекцию в файл
 */
public class SaveCommand implements Command {
    private CollectionManager cm;
    private String args;
    public SaveCommand(CollectionManager cm) { this.cm = cm; }
    @Override public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        if (args.isEmpty()) { System.out.println(ErrorMessages.commandError("save", ErrorMessages.FILE_NAME_REQUIRED)); return; }
        JsonUtil.writeToFile(args, cm.getCollection());
        System.out.println(ErrorMessages.savedToFile(args));
    }
    @Override 
    public String getDescription(){
        return "сохранить коллекцию в файл";
    }
}
