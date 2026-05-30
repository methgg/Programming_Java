package server.commands;

import exceptions.Messages;
import network.CommandRequest;
import network.CommandResponse;

public class ExitServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request) {
        return new CommandResponse(true, Messages.APP_EXIT, null);
    }

    @Override
    public String getDescription() {
        return "завершить программу";
    }
}
