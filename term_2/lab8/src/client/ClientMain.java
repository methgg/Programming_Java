package client;

import javax.swing.SwingUtilities;

import gui.GuiClientApp;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RequestSender requestSender = new SocketRequestSender("localhost", 25345);
            GuiClientApp guiClientApp = new GuiClientApp(requestSender);
            guiClientApp.start();
        });
        
    }
}
