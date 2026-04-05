package commands;

import manager.CollectionManager;
import util.JsonUtil;

public class SaveCommand implements Command {
    private CollectionManager cm;
    private String args;
    public SaveCommand(CollectionManager cm) { this.cm = cm; }
    @Override public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        if (args.isEmpty()) { System.out.println("Укажите имя файла."); return; }
        JsonUtil.writeToFile(args, cm.getCollection());
        System.out.println("Коллекция сохранена в " + args);
    }
    @Override 
    public String getDescription(){
        return "сохранить коллекцию в файл";
    }
}