package network;

import java.io.Serializable;

public class CommandRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final CommandType commandType;
    private final Serializable argument;

    public CommandRequest(CommandType commandType, Serializable argument){
        this.commandType = commandType;
        this.argument = argument;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public Serializable getArgument() {
        return argument;
    } 

}