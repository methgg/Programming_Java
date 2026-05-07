package server.commands;

import network.CommandRequest;
import network.CommandResponse;

public interface ServerCommand {
    CommandResponse execute(CommandRequest request);
    
    String getDescription();
}