package gui;

import client.RequestSender;

public class GuiClientApp{
    private final RequestSender requestSender;

    public GuiClientApp(RequestSender requestSender){
        this.requestSender = requestSender;
    }

    public void start() {
        LoginFrame loginFrame = new LoginFrame(requestSender);
        loginFrame.setVisible(true);
    }
}