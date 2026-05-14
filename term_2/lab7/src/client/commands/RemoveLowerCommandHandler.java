package client.commands;

import exceptions.CommandException;
import exceptions.ErrorMessages;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandType;
import network.arguments.MusicBandArgument;
import util.JsonUtil;
import util.ReadMusicBandFromUser;

public class RemoveLowerCommandHandler implements ClientCommandHandler {
    @Override
    public CommandRequest build(CommandType type, String commandName, String args, AuthData authData) {
        MusicBand referenceBand;

        if (!args.isBlank()) {
            referenceBand = JsonUtil.getGson().fromJson(args, MusicBand.class);
            if (referenceBand == null) {
                throw new CommandException(ErrorMessages.INSERT_PARSE_ERROR);
            }
        } else {
            referenceBand = new ReadMusicBandFromUser().read();
        }

        return new CommandRequest(type, new MusicBandArgument(referenceBand), authData);
    }
}
