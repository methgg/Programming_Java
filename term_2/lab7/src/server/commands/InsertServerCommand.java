package server.commands;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicBand;
import network.CommandRequest;
import network.CommandResponse;
import network.arguments.InsertArgument;

public class InsertServerCommand implements ServerCommand {
    private final CollectionManager cm;

    public InsertServerCommand(CollectionManager cm) {
        this.cm = cm;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            InsertArgument argument = (InsertArgument) request.getArgument();

            Long key = argument.getKey();
            MusicBand band = argument.getMusicBand();

            cm.insert(key, band);

            return new CommandResponse(true, ErrorMessages.ELEMENT_ADDED, null);
        } catch (ClassCastException | NullPointerException e) {
            return new CommandResponse(false, ErrorMessages.commandError("insert", ErrorMessages.INSERT_PARSE_ERROR), null);
        }
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент с заданным ключом";
    }
}
