package client;

public class ClientMain {
    public static void main(String[] args) {
        RequestSender requestSender = new SocketRequestSender("localhost", 25345);
        ClientApp clientApp = new ClientApp(requestSender);
        clientApp.start();
    }
}
