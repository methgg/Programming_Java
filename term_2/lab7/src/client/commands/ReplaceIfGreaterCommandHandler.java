package client.commands;

import exceptions.CommandException;
import exceptions.ErrorMessages;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandType;
import network.arguments.InsertArgument;
import util.JsonUtil;
import util.ReadMusicBandFromUser;

public class ReplaceIfGreaterCommandHandler implements ClientCommandHandler {
    @Override
    public CommandRequest build(CommandType type, String commandName, String args, AuthData authData) {
        if (args.isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
        }

        String[] replaceParts = args.split(" ", 2);
        Long key;

        try {
            key = Long.valueOf(replaceParts[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    ErrorMessages.commandError(commandName, ErrorMessages.INVALID_KEY)
            );
        }

        MusicBand newBand;

        if (replaceParts.length > 1) {
            newBand = JsonUtil.getGson().fromJson(replaceParts[1], MusicBand.class);
            if (newBand == null) {
                throw new CommandException(ErrorMessages.INSERT_PARSE_ERROR);
            }
        } else {
            newBand = new ReadMusicBandFromUser().read();
        }

        return new CommandRequest(type, new InsertArgument(key, newBand), authData);
    }
}
