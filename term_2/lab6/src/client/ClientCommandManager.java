package client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import network.CommandType;

public class ClientCommandManager {
    private final Map<String, CommandType> commands = new HashMap<>();

    public ClientCommandManager() {
        commands.put("help", CommandType.HELP);
        commands.put("info", CommandType.INFO);
        commands.put("show", CommandType.SHOW);
        commands.put("insert", CommandType.INSERT);
        commands.put("update", CommandType.UPDATE);
        commands.put("remove_key", CommandType.REMOVE_KEY);
        commands.put("clear", CommandType.CLEAR);
        commands.put("exit", CommandType.EXIT);
        commands.put("remove_lower", CommandType.REMOVE_LOWER);
        commands.put("replace_if_greater", CommandType.REPLACE_IF_GREATER);
        commands.put("remove_greater_key", CommandType.REMOVE_GREATER_KEY);
        commands.put("count_greater_than_number_of_participants", CommandType.COUNT_GREATER_THAN_NUMBER_OF_PARTICIPANTS);
        commands.put("filter_greater_than_genre", CommandType.FILTER_GREATER_THAN_GENRE);
        commands.put("print_field_descending_front_man", CommandType.PRINT_FIELD_DESCENDING_FRONT_MAN);
        commands.put("show_even", CommandType.SHOW_EVEN);
        commands.put("execute_script", null);
    }

    public CommandType getCommandType(String name) {
        return commands.get(name);
    }

    public Set<String> getCommandNames() {
        return commands.keySet();
    }

    public Map<String, CommandType> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}
