package client.commands;

import exceptions.CommandException;
import exceptions.Messages;
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
            throw new IllegalArgumentException(Messages.missingArgument(commandName));
        }

        String[] insertParts = args.split(" ", 2);
        Long key;

        try {
            key = Long.valueOf(insertParts[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    Messages.commandError(commandName, Messages.INVALID_KEY)
            );
        }

        MusicBand band;

        if (insertParts.length > 1) {
            band = JsonUtil.getGson().fromJson(insertParts[1], MusicBand.class);
            if (band == null) {
                throw new CommandException(Messages.INSERT_PARSE_ERROR);
            }
        } else {
            band = new ReadMusicBandFromUser().read();
        }

        return new CommandRequest(type, new InsertArgument(key, band), authData);
    }
}
