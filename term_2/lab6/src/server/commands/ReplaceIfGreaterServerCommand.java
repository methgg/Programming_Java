package server.commands;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.InsertArgument;

public class ReplaceIfGreaterServerCommand implements ServerCommand {
    private final CollectionManager cm;

    public ReplaceIfGreaterServerCommand(CollectionManager cm) {
        this.cm = cm;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            InsertArgument argument = (InsertArgument) request.getArgument();

            Long key = argument.getKey();
            MusicBand newBand = argument.getMusicBand();

            if (!cm.getCollection().containsKey(key)) {
                return new CommandResponse(false, ErrorMessages.elementNotFound(key), null);
            }

            MusicBand oldBand = cm.getCollection().get(key);

            if (newBand.compareTo(oldBand) > 0) {
                cm.getCollection().put(key, newBand);
                return new CommandResponse(true, ErrorMessages.ELEMENT_REPLACED, null);
            }

            return new CommandResponse(true, ErrorMessages.NEW_ELEMENT_NOT_GREATER, null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(
                    false,
                    ErrorMessages.commandError("replace_if_greater", ErrorMessages.INSERT_PARSE_ERROR),
                    null
            );
        }
    }

    @Override
    public String getDescription() {
        return "заменить значение по ключу, если новое значение больше старого";
    }
}
