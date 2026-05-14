package client.commands;

import network.AuthData;
import network.CommandRequest;
import network.CommandType;

public interface ClientCommandHandler {
    CommandRequest build(CommandType type, String commandName, String args, AuthData authData);
}
