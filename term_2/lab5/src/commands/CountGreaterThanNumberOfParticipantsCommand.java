package commands;

import manager.CollectionManager;

/**
 * Печатает количество элементов, у которых numberOfParticipants больше заданного числа.
 */
public class CountGreaterThanNumberOfParticipantsCommand implements Command {
    private CollectionManager cm;
    private String args;

    public CountGreaterThanNumberOfParticipantsCommand(CollectionManager cm) { 
        this.cm = cm;
    }

    @Override
    public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        try {
            int number = Integer.parseInt(args.trim());
            long count = cm.getCollection().values().stream()
                    .filter(b -> b.getNumberOfParticipants() > number)
                    .count();
            System.out.println("Количество элементов с numberOfParticipants > " + number + ": " + count);
        } catch (Exception e) { System.out.println("Ошибка: " + e.getMessage()); }
    }
}