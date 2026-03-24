package commands;
public class ExitCommand implements Command {
    private String args;

    @Override
    public void setArgs(String args) { 
        this.args = args;
    }

    @Override
    public void execute() {
        System.out.println("Работа консольного приложения завершена");
        System.exit(0);
    }
}