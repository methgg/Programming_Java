package server.commands;

import java.util.List;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.MusicBandArgument;

public class RemoveLowerServerCommand implements ServerCommand {
    private final CollectionManager cm;

    public RemoveLowerServerCommand(CollectionManager cm) {
        this.cm = cm;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            MusicBandArgument argument = (MusicBandArgument) request.getArgument();
            MusicBand referenceBand = argument.getMusicBand();

            List<Long> keysToRemove = cm.getCollection().entrySet().stream().filter(entry -> entry.getValue().compareTo(referenceBand) < 0).map(entry -> entry.getKey()).toList();

            keysToRemove.forEach(cm.getCollection()::remove);

            return new CommandResponse(true, ErrorMessages.REMOVE_LOWER_DONE, null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(
                    false,
                    ErrorMessages.commandError("remove_lower", ErrorMessages.INSERT_PARSE_ERROR),
                    null
            );
        }
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
