package client;

import manager.CollectionManager;
import server.ServerCommandManager;
import server.ServerCommandProcessor;

public class ClientMain {
    public static void main(String[] args) {
        CollectionManager cm = new CollectionManager();
        ServerCommandManager serverCommandManager = new ServerCommandManager(cm);
        ServerCommandProcessor serverCommandProcessor = new ServerCommandProcessor(serverCommandManager);

        RequestSender requestSender = new LocalRequestSender(serverCommandProcessor);
        ClientApp clientApp = new ClientApp(requestSender);
        clientApp.start();
    }
}