package server;

import java.util.HashMap;
import java.util.Map;

import manager.CollectionManager;
import network.CommandType;
import server.commands.ClearServerCommand;
import server.commands.InfoServerCommand;
import server.commands.ServerCommand;
import server.commands.ShowServerCommand;

public class ServerCommandManager {
    private final Map<CommandType, ServerCommand> commands = new HashMap<>();

    public ServerCommandManager(CollectionManager collectionManager) {
        commands.put(CommandType.INFO, new InfoServerCommand(collectionManager));
        commands.put(CommandType.SHOW, new ShowServerCommand(collectionManager));
        commands.put(CommandType.CLEAR, new ClearServerCommand(collectionManager));
    }

    public ServerCommand getCommand(CommandType commandType) {
        return commands.get(commandType);
    }

    public Map<CommandType, ServerCommand> getCommands() {
        return commands;
    }
}
