package client.commands;

import exceptions.CommandException;
import exceptions.ErrorMessages;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandType;
import network.arguments.UpdateArgument;
import util.JsonUtil;
import util.ReadMusicBandFromUser;

public class UpdateCommandHandler implements ClientCommandHandler {
    @Override
    public CommandRequest build(CommandType type, String commandName, String args, AuthData authData) {
        if (args.isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.missingArgument(commandName));
        }

        String[] updateParts = args.split(" ", 2);
        Long id;

        try {
            id = Long.valueOf(updateParts[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    ErrorMessages.commandError(commandName, ErrorMessages.INVALID_KEY)
            );
        }

        MusicBand updatedBand;

        if (updateParts.length > 1) {
            updatedBand = JsonUtil.getGson().fromJson(updateParts[1], MusicBand.class);
            if (updatedBand == null) {
                throw new CommandException(ErrorMessages.INSERT_PARSE_ERROR);
            }
        } else {
            updatedBand = new ReadMusicBandFromUser().read();
        }

        return new CommandRequest(type, new UpdateArgument(id, updatedBand), authData);
    }
}
