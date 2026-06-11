package server;

import exceptions.Messages;
import network.CommandRequest;
import network.CommandResponse;
import server.commands.ServerCommand;


public class ServerCommandProcessor {
    private final ServerCommandManager commandManager;
    private final AuthService authService;
    public ServerCommandProcessor(ServerCommandManager commandManager, AuthService authService) {
        this.commandManager = commandManager;
        this.authService = authService;
    }

    public CommandResponse process(CommandRequest request) {
        if (request == null || request.getCommandType() == null) {
            return new CommandResponse(false, Messages.invalidRequest(), null);
        }

        if (request.getCommandType() != network.CommandType.REGISTER && request.getCommandType() != network.CommandType.LOGIN) {
            try {
                String authError = authService.validate(request.getAuthData());
                if (authError != null) {
                    return new CommandResponse(false, authError, null);
                }
            } catch (Exception e) {
                return new CommandResponse(false, Messages.commandExecutionError(e.getMessage()), null);
            }
        }


        ServerCommand command = commandManager.getCommand(request.getCommandType());

        if (command == null) {
            return new CommandResponse(false, Messages.UNKNOWN_COMMAND, null);
        }

        try {
            return command.execute(request);
        } catch (Exception e) {
            return new CommandResponse(false, Messages.commandExecutionError(e.getMessage()), null);
        }
    }
}
