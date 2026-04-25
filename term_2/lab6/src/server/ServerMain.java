package server;

import manager.CollectionManager;
import util.IdGenerator;
import util.JsonUtil;

public class ServerMain {
    public static void main(String[] args) {
        CollectionManager cm = new CollectionManager();
        String filename = System.getenv("COLLECTIONFILE");

        if (filename != null) {
            cm.getCollection().putAll(JsonUtil.readFromFile(filename));
            cm.getCollection().forEach((key, band) -> band.setId(key));
            long maxId = cm.getCollection().keySet().stream().mapToLong(Long::longValue).max().orElse(0);
            IdGenerator.updateCurrentId(maxId);
        }

        ServerCommandManager serverCommandManager = new ServerCommandManager(cm);
        ServerCommandProcessor serverCommandProcessor = new ServerCommandProcessor(serverCommandManager);
        ServerTcpApp serverTcpApp = new ServerTcpApp(serverCommandProcessor, 12345);
        serverTcpApp.start();

      
    }
}