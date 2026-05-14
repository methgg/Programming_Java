package client.commands;

import exceptions.ErrorMessages;
import network.AuthData;
import network.CommandRequest;
import network.CommandType;
import network.arguments.NumberOfParticipantsArgument;

public class NumberOfParticipantsCommandHandler implements ClientCommandHandler {
    @Override
    public CommandRequest build(CommandType type, String commandName, String args, AuthData authData) {
        if (args.isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
        }

        try {
            return new CommandRequest(type, new NumberOfParticipantsArgument(Integer.valueOf(args)), authData);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    ErrorMessages.commandError(commandName, ErrorMessages.INVALID_NUMBER)
            );
        }
    }
}
