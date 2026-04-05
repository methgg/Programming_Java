package commands;

import java.util.Map;

import manager.CommandManager;

public class HelpCommand implements Command {
    private String args;
    private CommandManager commandManager;
    public HelpCommand(CommandManager commandManager)
    {
        this.commandManager = commandManager;
    }

    @Override 
    public void setArgs(String args) {
         this.args = args; 
    }

    @Override public void execute() {
        System.out.println("Доступные команды:");
        commandManager.getCommands().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> System.out.println(entry.getKey()+ ":" + entry.getValue().getDescription()));
                
    }
    @Override 
    public String getDescription(){
        return "вывести справку по доступным командам";
    }
}