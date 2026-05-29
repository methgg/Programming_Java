package client.commands;

import exceptions.Messages;
import models.MusicGenre;
import network.AuthData;
import network.CommandRequest;
import network.CommandType;
import network.arguments.GenreArgument;

public class GenreCommandHandler implements ClientCommandHandler {
    @Override
    public CommandRequest build(CommandType type, String commandName, String args, AuthData authData) {
        if (args.isBlank()) {
            throw new IllegalArgumentException(Messages.missingArgument(commandName));
        }

        try {
            return new CommandRequest(type, new GenreArgument(MusicGenre.valueOf(args.toUpperCase())), authData);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    Messages.commandError(commandName, Messages.INVALID_GENRE)
            );
        }
    }
}
