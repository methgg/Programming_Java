package server.commands;

import java.util.stream.Collectors;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import network.CommandRequest;
import network.CommandResponse;

public class ShowEvenServerCommand implements ServerCommand {
    private final CollectionManager cm;

    public ShowEvenServerCommand(CollectionManager cm) {
        this.cm = cm;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String message = cm.getCollection().entrySet().stream()
                .filter(entry -> entry.getKey() % 2 == 0)
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .collect(Collectors.joining("\n"));

        if (message.isEmpty()) {
            message = ErrorMessages.COLLECTION_EMPTY;
        }

        return new CommandResponse(true, message, null);
    }

    @Override
    public String getDescription() {
        return "возвращает элементы с четными ключами";
    }
}
