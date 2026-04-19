package commands;

import manager.CollectionManager;

/**
 * Класс, который реализует команду, которая выводит информацию о коллекции
 */
public class InfoCommand implements Command {
    private String args;

    @Override
    public void setArgs(String args) { 
        this.args = args;
    }

    private final CollectionManager cm;


    public InfoCommand(CollectionManager cm) {
        this.cm = cm;

    }

    @Override
    public void execute() {
        System.out.println("=== Информация о коллекции ===");
        System.out.println("Тип коллекции: " + cm.getCollection().getClass().getSimpleName());
        System.out.println("Класс хранимых в коллекции объектов: " + cm.getCollection().getClass());
        System.out.println("Дата инициализации коллекции: " + cm.getInitializationDate());
        System.out.println("Количество элементов: " + cm.size());

        }
    @Override 
    public String getDescription(){
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
    }

