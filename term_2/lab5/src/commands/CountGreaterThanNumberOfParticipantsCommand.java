package commands;

import exceptions.ErrorMessages;
import manager.CollectionManager;

public class CountGreaterThanNumberOfParticipantsCommand implements Command {
    private CollectionManager cm;
    private String args;

    public CountGreaterThanNumberOfParticipantsCommand(CollectionManager cm) { 
        this.cm = cm;
    }

    @Override
    public void setArgs(String args) { 
        this.args = args;
    }

    @Override
    public void execute() {
        try {
            int number = Integer.parseInt(args.trim());
            long count = cm.getCollection().values().stream()
                    .filter(b -> b.getNumberOfParticipants() > number)
                    .count();
            System.out.println(ErrorMessages.countGreaterThanParticipants(number, count));
        } catch (NumberFormatException e) {
            System.out.println(ErrorMessages.commandError("count_greater_than_number_of_participants", ErrorMessages.INVALID_NUMBER));
        }
    }

    @Override 
    public String getDescription(){
        return "вывести количество элементов, значение поля numberOfParticipants которых больше заданного";
    }
}
