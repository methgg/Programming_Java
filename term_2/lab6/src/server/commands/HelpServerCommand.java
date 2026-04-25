package server.commands;

import java.util.Map;
import java.util.stream.Collectors;

import exceptions.ErrorMessages;
import network.CommandRequest;
import network.CommandResponse;
import network.CommandType;

public class HelpServerCommand implements ServerCommand {
    private final Map<CommandType, ServerCommand> commands;

    public HelpServerCommand(Map<CommandType, ServerCommand> commands) {
        this.commands = commands;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String message = ErrorMessages.AVAILABLE_COMMANDS + "\n"
                + commands.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(entry -> entry.getKey().name().toLowerCase() + ": " + entry.getValue().getDescription())
                        .collect(Collectors.joining("\n"));

        return new CommandResponse(true, message, null);
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}
