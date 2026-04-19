package commands;

import exceptions.ErrorMessages;

/**
 * Класс, который реализует команду, которая звершает работу программы
 */
public class ExitCommand implements Command {
    private String args;

    @Override
    public void setArgs(String args) { 
        this.args = args;
    }

    @Override
    public void execute() {
        System.out.println(ErrorMessages.APP_EXIT);
        System.exit(0);
    }

    @Override 
    public String getDescription(){
        return "завершить программу (без сохранения в файл)";
    }
}
