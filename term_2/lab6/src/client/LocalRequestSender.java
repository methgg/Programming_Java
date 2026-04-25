package client;

import network.CommandRequest;
import network.CommandResponse;
import server.ServerCommandProcessor;

public class LocalRequestSender implements RequestSender {
    private final ServerCommandProcessor serverCommandProcessor;

    public LocalRequestSender(ServerCommandProcessor serverCommandProcessor) {
        this.serverCommandProcessor = serverCommandProcessor;
    }

    @Override
    public CommandResponse send(CommandRequest request) {
        return serverCommandProcessor.process(request);
    }

}