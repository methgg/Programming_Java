package server;

import java.util.HashMap;
import java.util.Map;

import manager.CollectionManager;
import network.CommandType;
import server.commands.ClearServerCommand;
import server.commands.CountGreaterThanNumberOfParticipantsServerCommand;
import server.commands.ExitServerCommand;
import server.commands.FilterGreaterThanGenreServerCommand;
import server.commands.HelpServerCommand;
import server.commands.InfoServerCommand;
import server.commands.InsertServerCommand;
import server.commands.PrintFieldDescendingFrontManServerCommand;
import server.commands.RemoveGreaterKeyServerCommand;
import server.commands.RemoveKeyServerCommand;
import server.commands.RemoveLowerServerCommand;
import server.commands.ReplaceIfGreaterServerCommand;
import server.commands.ServerCommand;
import server.commands.ShowEvenServerCommand;
import server.commands.ShowServerCommand;
import server.commands.UpdateServerCommand;


public class ServerCommandManager {
    private final Map<CommandType, ServerCommand> commands = new HashMap<>();

    public ServerCommandManager(CollectionManager collectionManager) {
        commands.put(CommandType.INFO, new InfoServerCommand(collectionManager));
        commands.put(CommandType.SHOW, new ShowServerCommand(collectionManager));
        commands.put(CommandType.CLEAR, new ClearServerCommand(collectionManager));
        commands.put(CommandType.COUNT_GREATER_THAN_NUMBER_OF_PARTICIPANTS, new CountGreaterThanNumberOfParticipantsServerCommand(collectionManager));
        commands.put(CommandType.REMOVE_KEY, new RemoveKeyServerCommand(collectionManager));
        commands.put(CommandType.REMOVE_GREATER_KEY, new RemoveGreaterKeyServerCommand(collectionManager));
        commands.put(CommandType.FILTER_GREATER_THAN_GENRE, new FilterGreaterThanGenreServerCommand(collectionManager));
        commands.put(CommandType.PRINT_FIELD_DESCENDING_FRONT_MAN, new PrintFieldDescendingFrontManServerCommand(collectionManager));
        commands.put(CommandType.SHOW_EVEN, new ShowEvenServerCommand(collectionManager));
        commands.put(CommandType.EXIT, new ExitServerCommand());
        commands.put(CommandType.HELP, new HelpServerCommand(commands));
        commands.put(CommandType.INSERT, new InsertServerCommand(collectionManager));
        commands.put(CommandType.UPDATE, new UpdateServerCommand(collectionManager));
        commands.put(CommandType.REMOVE_LOWER, new RemoveLowerServerCommand(collectionManager));
        commands.put(CommandType.REPLACE_IF_GREATER, new ReplaceIfGreaterServerCommand(collectionManager));


    }



    public ServerCommand getCommand(CommandType commandType) {
        return commands.get(commandType);
    }

    public Map<CommandType, ServerCommand> getCommands() {
        return commands;
    }
}
