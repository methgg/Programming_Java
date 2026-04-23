package client;

import exceptions.ErrorMessages;
import models.MusicGenre;
import network.CommandRequest;
import network.CommandType;
import network.arguments.GenreArgument;
import network.arguments.KeyArgument;
import network.arguments.NumberOfParticipantsArgument;

public class ClientCommandParser {
    private final ClientCommandManager commandManager;
    
    public ClientCommandParser(ClientCommandManager commandManager){
        this.commandManager = commandManager;
    }

    public CommandRequest parse(String line) {
    String[] parts = line.trim().split(" ", 2);
    String commandName = parts[0];
    String args = parts.length > 1 ? parts[1].trim() : "";

    CommandType type = commandManager.getCommandType(commandName);

    if (type == null) {
        throw new IllegalArgumentException(ErrorMessages.UNKNOWN_COMMAND);
    }

    try {
        switch (type) {
            case HELP:
            case INFO:
            case SHOW:
            case CLEAR:
            case EXIT:
            case PRINT_FIELD_DESCENDING_FRONT_MAN:
            case SHOW_EVEN:
                return new CommandRequest(type, null);

            case REMOVE_KEY:
            case REMOVE_GREATER_KEY:
                if (args.isEmpty()) {
                    throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
                }
                return new CommandRequest(type, new KeyArgument(Long.valueOf(args)));

            case COUNT_GREATER_THAN_NUMBER_OF_PARTICIPANTS:
                if (args.isEmpty()) {
                    throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
                }
                return new CommandRequest(type, new NumberOfParticipantsArgument(Integer.valueOf(args)));

            case FILTER_GREATER_THAN_GENRE:
                if (args.isEmpty()) {
                    throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
                }
                try {
                    return new CommandRequest(type, new GenreArgument(MusicGenre.valueOf(args.toUpperCase())));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(ErrorMessages.commandError(commandName, ErrorMessages.INVALID_GENRE));
                }

            default:
                throw new IllegalArgumentException(ErrorMessages.unsupportedClientCommand(commandName));
        }
    } catch (NumberFormatException e) {
        if (type == CommandType.COUNT_GREATER_THAN_NUMBER_OF_PARTICIPANTS) {
            throw new IllegalArgumentException(ErrorMessages.commandError(commandName, ErrorMessages.INVALID_NUMBER));
        }
        throw new IllegalArgumentException(ErrorMessages.commandError(commandName, ErrorMessages.INVALID_KEY));
    }

    } 
}
