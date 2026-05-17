package server.commands;

import java.util.concurrent.locks.Lock;

import exceptions.ErrorMessages;
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
        Lock readLock = collectionManager.getLock().readLock();
        readLock.lock();
        try {
            NumberOfParticipantsArgument argument = (NumberOfParticipantsArgument) request.getArgument();
            int number = argument.getNumberOfParticipants();
            long count = collectionManager.getCollection().values().stream().filter(band -> band.getNumberOfParticipants() > number).count();

            return new CommandResponse(true, ErrorMessages.countGreaterThanParticipants(number, count), null);
            
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(false, ErrorMessages.commandError("count_greater_than_number_of_participants", ErrorMessages.INVALID_NUMBER), null);

        } finally {
            readLock.unlock();
        }
    }
    @Override 
    public String getDescription() {
        return "вывести количество элементов, значение поля numberOfParticipants которых больше заданного";
        
    }
}

