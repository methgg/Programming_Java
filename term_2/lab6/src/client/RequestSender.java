package client;

import network.CommandRequest;
import network.CommandResponse;

public interface RequestSender {
    CommandResponse send(CommandRequest request);
}
