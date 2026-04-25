package server.commands;

import java.util.Iterator;
import java.util.Map;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
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
            Iterator<Map.Entry<Long, MusicBand>> it = cm.getCollection().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Long, MusicBand> entry = it.next();
                if (entry.getKey() > key) it.remove();
            }
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