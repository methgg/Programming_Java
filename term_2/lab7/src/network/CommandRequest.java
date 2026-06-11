package network;

import java.io.Serializable;

public class CommandRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final CommandType commandType;
    private final Serializable argument;
    private final AuthData authData;

    public CommandRequest(CommandType commandType, Serializable argument, AuthData authData){
        this.commandType = commandType;
        this.argument = argument;
        this.authData = authData;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public Serializable getArgument() {
        return argument;
    } 

    public AuthData getAuthData() {
        return authData;
    }

}