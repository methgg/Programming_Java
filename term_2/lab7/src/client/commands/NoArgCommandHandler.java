package client.commands;

import exceptions.Messages;
import network.AuthData;
import network.CommandRequest;
import network.CommandType;

public class NoArgCommandHandler implements ClientCommandHandler {
    @Override
    public CommandRequest build(CommandType type, String commandName, String args, AuthData authData) {
        if (!args.isBlank()) {
            throw new IllegalArgumentException(Messages.commandError(commandName, "Эта команда не принимает аргументы."));
        }

        return new CommandRequest(type, null, authData);
    }
}