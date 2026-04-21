package server.commands;

import java.util.Iterator;
import java.util.Map;

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

            Iterator<Map.Entry<Long, MusicBand>> iterator =
                    cm.getCollection().entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<Long, MusicBand> entry = iterator.next();

                if (entry.getValue().compareTo(referenceBand) < 0) {
                    iterator.remove();
                }
            }

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
