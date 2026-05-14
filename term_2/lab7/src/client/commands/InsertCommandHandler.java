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

public class InsertCommandHandler implements ClientCommandHandler {
    @Override
    public CommandRequest build(CommandType type, String commandName, String args, AuthData authData) {
        if (args.isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
        }

        String[] insertParts = args.split(" ", 2);
        Long key;

        try {
            key = Long.valueOf(insertParts[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    ErrorMessages.commandError(commandName, ErrorMessages.INVALID_KEY)
            );
        }

        MusicBand band;

        if (insertParts.length > 1) {
            band = JsonUtil.getGson().fromJson(insertParts[1], MusicBand.class);
            if (band == null) {
                throw new CommandException(ErrorMessages.INSERT_PARSE_ERROR);
            }
        } else {
            band = new ReadMusicBandFromUser().read();
        }

        return new CommandRequest(type, new InsertArgument(key, band), authData);
    }
}
