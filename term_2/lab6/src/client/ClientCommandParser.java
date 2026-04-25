package client;

import exceptions.CommandException;
import exceptions.ErrorMessages;
import models.MusicBand;
import models.MusicGenre;
import network.CommandRequest;
import network.CommandType;
import network.arguments.GenreArgument;
import network.arguments.InsertArgument;
import network.arguments.KeyArgument;
import network.arguments.MusicBandArgument;
import network.arguments.NumberOfParticipantsArgument;
import network.arguments.UpdateArgument;
import util.JsonUtil;
import util.ReadMusicBandFromUser;

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

            case INSERT:
                if(args.isEmpty()) {
                    throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
                }   
                String[] insertParts = args.split(" ", 2);
                Long key = Long.valueOf(insertParts[0]); 

                MusicBand band;
                if (insertParts.length > 1) {
                    band = JsonUtil.getGson().fromJson(insertParts[1], MusicBand.class);
                    if (band == null) {
                        throw new CommandException(ErrorMessages.INSERT_PARSE_ERROR);
                    }
                }
                else {
                    band = new ReadMusicBandFromUser().read();
                }

                return new CommandRequest(type, new InsertArgument(key, band));

            case UPDATE:
                if(args.isEmpty()) {
                    throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
                } 
                String[] updateParts = args.split(" ", 2);
                Long id = Long.valueOf(updateParts[0]);

                MusicBand updatedBand;
                if (updateParts.length > 1) {
                    updatedBand = JsonUtil.getGson().fromJson(updateParts[1], MusicBand.class);
                    if (updatedBand == null) {
                        throw new CommandException(ErrorMessages.INSERT_PARSE_ERROR);
                    }
                }
                else {
                    updatedBand = new ReadMusicBandFromUser().read();
                }
                return new CommandRequest(type, new UpdateArgument(id, updatedBand));

            case REMOVE_LOWER:
                MusicBand referenceBand;

                if (!args.isEmpty()) {
                    referenceBand = JsonUtil.getGson().fromJson(args, MusicBand.class);
                    if (referenceBand == null) {
                        throw new CommandException(ErrorMessages.INSERT_PARSE_ERROR);
                    }
                } else {
                    referenceBand = new ReadMusicBandFromUser().read();
                }

                return new CommandRequest(type, new MusicBandArgument(referenceBand));

            case REPLACE_IF_GREATER:
                if (args.isEmpty()) {
                    throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
                }

                String[] replaceParts = args.split(" ", 2);
                Long replaceKey = Long.valueOf(replaceParts[0]);

                MusicBand newBand;

                if (replaceParts.length > 1) {
                    newBand = JsonUtil.getGson().fromJson(replaceParts[1], MusicBand.class);
                    if (newBand == null) {
                        throw new CommandException(ErrorMessages.INSERT_PARSE_ERROR);
                    }
                } else {
                    newBand = new ReadMusicBandFromUser().read();
                }

                return new CommandRequest(type, new InsertArgument(replaceKey, newBand));

            
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
