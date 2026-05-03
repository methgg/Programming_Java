package server.commands;

import java.util.Map;
import java.util.Optional;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.UpdateArgument;

public class UpdateServerCommand implements ServerCommand {
    private final CollectionManager cm;

    public UpdateServerCommand(CollectionManager cm) {
        this.cm = cm;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            UpdateArgument argument = (UpdateArgument) request.getArgument();

            Long id = argument.getId();
            MusicBand newBand = argument.getMusicBand();

            Optional<Map.Entry<Long, MusicBand>> existingEntry = cm.getCollection().entrySet().stream()
                    .filter(entry -> entry.getValue().getId().equals(id))
                    .findFirst();

            if (existingEntry.isEmpty()) {
                return new CommandResponse(false, ErrorMessages.elementWithIdNotFound(id), null);
            }

            newBand.setId(id);
            cm.getCollection().put(existingEntry.get().getKey(), newBand);

            return new CommandResponse(true, ErrorMessages.updatedById(id), null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(
                    false,
                    ErrorMessages.commandError("update", ErrorMessages.INVALID_ID),
                    null
            );
        }
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
