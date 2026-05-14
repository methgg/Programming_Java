package client.commands;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import network.CommandType;

public class ClientCommandHandlerRegistry {
    private final Map<CommandType, ClientCommandHandler> handlers = new EnumMap<>(CommandType.class);

    public ClientCommandHandlerRegistry() {
        ClientCommandHandler noArgHandler = new NoArgCommandHandler();
        ClientCommandHandler keyHandler = new KeyCommandHandler();
        ClientCommandHandler numberHandler = new NumberOfParticipantsCommandHandler();
        ClientCommandHandler genreHandler = new GenreCommandHandler();
        ClientCommandHandler insertHandler = new InsertCommandHandler();
        ClientCommandHandler updateHandler = new UpdateCommandHandler();
        ClientCommandHandler removeLowerHandler = new RemoveLowerCommandHandler();
        ClientCommandHandler replaceIfGreaterHandler = new ReplaceIfGreaterCommandHandler();



        Set<CommandType> noArgCommands = Set.of(
                CommandType.HELP,
                CommandType.INFO,
                CommandType.SHOW,
                CommandType.CLEAR,
                CommandType.EXIT,
                CommandType.PRINT_FIELD_DESCENDING_FRONT_MAN,
                CommandType.SHOW_EVEN
        );

        for (CommandType type : noArgCommands) {
            handlers.put(type, noArgHandler);
        }

        handlers.put(CommandType.REMOVE_KEY, keyHandler);
        handlers.put(CommandType.REMOVE_GREATER_KEY, keyHandler);
        handlers.put(CommandType.COUNT_GREATER_THAN_NUMBER_OF_PARTICIPANTS, numberHandler);
        handlers.put(CommandType.FILTER_GREATER_THAN_GENRE, genreHandler);
        handlers.put(CommandType.INSERT, insertHandler);
        handlers.put(CommandType.UPDATE, updateHandler);
        handlers.put(CommandType.REMOVE_LOWER, removeLowerHandler);
        handlers.put(CommandType.REPLACE_IF_GREATER, replaceIfGreaterHandler);


    }

    public ClientCommandHandler getHandler(CommandType type) {
        return handlers.get(type);
    }
}
