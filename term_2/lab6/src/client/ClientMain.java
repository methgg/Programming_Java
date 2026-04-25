package client;

import manager.CollectionManager;

public class ClientMain {
    public static void main(String[] args) {
        CollectionManager cm = new CollectionManager();
        ClientApp clientApp = new ClientApp(cm);
        clientApp.start();
    }
}