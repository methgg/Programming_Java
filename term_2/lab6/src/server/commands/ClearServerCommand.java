package server.commands;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import network.CommandRequest;
import network.CommandResponse;

public class ClearServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;

    public ClearServerCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        collectionManager.clear();
        return new CommandResponse(true, ErrorMessages.COLLECTION_CLEARED, null);
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}
