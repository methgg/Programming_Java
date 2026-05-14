package client.commands;

import exceptions.ErrorMessages;
import network.AuthData;
import network.CommandRequest;
import network.CommandType;
import network.arguments.KeyArgument;

public class KeyCommandHandler implements ClientCommandHandler {
    @Override
    public CommandRequest build(CommandType type, String commandName, String args, AuthData authData) {
        if (args.isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
        }

        try {
            return new CommandRequest(type, new KeyArgument(Long.valueOf(args)), authData);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    ErrorMessages.commandError(commandName, ErrorMessages.INVALID_KEY)
            );
        }
    }
}
