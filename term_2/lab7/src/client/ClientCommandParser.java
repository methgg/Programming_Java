package client;

import client.commands.ClientCommandHandler;
import client.commands.ClientCommandHandlerRegistry;
import exceptions.ErrorMessages;
import network.AuthData;
import network.CommandRequest;
import network.CommandType;

public class ClientCommandParser {
    private final ClientCommandManager commandManager;
    private final ClientCommandHandlerRegistry handlerRegistry;
    private final AuthData authData;

    public ClientCommandParser(ClientCommandManager commandManager, AuthData authData) {
        this.commandManager = commandManager;
        this.authData = authData;
        this.handlerRegistry = new ClientCommandHandlerRegistry();
    }


    public CommandRequest parse(String line) {
        String[] parts = line.trim().split(" ", 2);
        String commandName = parts[0];
        String args = parts.length > 1 ? parts[1].trim() : "";

        CommandType type = commandManager.getCommandType(commandName);

        if (type == null) {
            throw new IllegalArgumentException(ErrorMessages.UNKNOWN_COMMAND);
        }

        ClientCommandHandler handler = handlerRegistry.getHandler(type);
        if (handler != null) {
            return handler.build(type, commandName, args, authData);
        }
        
        throw new IllegalArgumentException(ErrorMessages.unsupportedClientCommand(commandName));
    } 
}
