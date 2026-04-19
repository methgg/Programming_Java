package commands;

import manager.CollectionManager;

public class ShowEvenCommand implements Command {
    private CollectionManager cm;
    private String args;
    public ShowEvenCommand(CollectionManager cm){
        this.cm = cm;
    }
    @Override 
    public void setArgs(String args) { 
        this.args = args;
    }
    @Override
    public void execute(){
        cm.getCollection().entrySet().stream().filter(e -> e.getKey() % 2 == 0).forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
    } 

    @Override
    public String getDescription(){
        return "возвращает элементы с четными ключами";
    }


}