package server;

import exceptions.ErrorMessages;
import network.CommandRequest;
import network.CommandResponse;
import server.commands.ServerCommand;

public class ServerCommandProcessor {
    private final ServerCommandManager commandManager;

    public ServerCommandProcessor(ServerCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public CommandResponse process(CommandRequest request) {
        if (request == null || request.getCommandType() == null) {
            return new CommandResponse(false, ErrorMessages.invalidRequest(), null);

        }

        ServerCommand command = commandManager.getCommand(request.getCommandType());

        if (command == null) {
            return new CommandResponse(false, ErrorMessages.UNKNOWN_COMMAND, null);
        }

        try {
            return command.execute(request);
        } catch (Exception e) {
            return new CommandResponse(false, ErrorMessages.commandExecutionError(e.getMessage()), null);
        }
    }
}
