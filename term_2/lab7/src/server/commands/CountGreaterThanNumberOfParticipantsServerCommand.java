package server.commands;

import exceptions.Messages;
import manager.CollectionManager;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.NumberOfParticipantsArgument;

public class CountGreaterThanNumberOfParticipantsServerCommand implements ServerCommand{
    private final CollectionManager collectionManager;

    public CountGreaterThanNumberOfParticipantsServerCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    @Override 
    public CommandResponse execute(CommandRequest request) {
        return collectionManager.withReadLock(() -> {
            try {
                NumberOfParticipantsArgument argument = (NumberOfParticipantsArgument) request.getArgument();
                int number = argument.getNumberOfParticipants();
                long count = collectionManager.getCollection().values().stream().filter(band -> band.getNumberOfParticipants() > number).count();

                return new CommandResponse(true, Messages.countGreaterThanParticipants(number, count), null);
            } catch (ClassCastException | NullPointerException e) {
                return new CommandResponse(false, Messages.commandError("count_greater_than_number_of_participants", Messages.INVALID_NUMBER), null);
            }
        });
    }
    @Override 
    public String getDescription() {
        return "вывести количество элементов, значение поля numberOfParticipants которых больше заданного";
        
    }
}
