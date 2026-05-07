package server.commands;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.KeyArgument;

public class RemoveKeyServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;

    public RemoveKeyServerCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            KeyArgument argument = (KeyArgument) request.getArgument();
            Long key = argument.getKey();

            collectionManager.remove(key);

            return new CommandResponse(true, ErrorMessages.removedByKey(key), null);

        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(false, ErrorMessages.commandError("remove_key", ErrorMessages.INVALID_KEY), null);
        }
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его ключу";
    }
}