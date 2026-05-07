package server.commands;


import java.util.List;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.KeyArgument;

public class RemoveGreaterKeyServerCommand implements ServerCommand {
    private final CollectionManager cm;

    public RemoveGreaterKeyServerCommand(CollectionManager cm) {
        this.cm = cm;
    }

    @Override 
    public CommandResponse execute(CommandRequest request) {
        try {
            KeyArgument argument = (KeyArgument) request.getArgument();
            Long key = argument.getKey();
            List<Long> keysToRemove = cm.getCollection().entrySet().stream().filter(entry -> entry.getKey() > key).map(entry -> entry.getKey()).toList();
            keysToRemove.forEach(cm.getCollection()::remove);
            return new CommandResponse(true, ErrorMessages.removedGreaterKeys(key), null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(false, ErrorMessages.commandError("remove_greater_key", ErrorMessages.INVALID_KEY), null);
        }
    }

    @Override 
    public String getDescription(){
        return "удалить из коллекции все элементы, ключ которых превышает заданный";
    }
}