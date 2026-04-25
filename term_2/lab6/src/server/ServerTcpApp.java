package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import exceptions.ErrorMessages;
import network.CommandRequest;
import network.CommandResponse;

public class ServerTcpApp {
    private final ServerCommandProcessor serverCommandProcessor;
    private final int port;

    public ServerTcpApp(ServerCommandProcessor serverCommandProcessor, int port) {
        this.serverCommandProcessor = serverCommandProcessor;
        this.port = port;
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер слушает порт " + port + ".");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиент подключен.");

                try (
                        var input = new java.io.ObjectInputStream(clientSocket.getInputStream());
                        var output = new java.io.ObjectOutputStream(clientSocket.getOutputStream())
                ) {
                    Object obj = input.readObject();
                    CommandResponse response;

                    if (obj instanceof CommandRequest request) {
                        response = serverCommandProcessor.process(request);
                    } else {
                        response = new CommandResponse(false, ErrorMessages.invalidRequest(), null);
                    }

                    output.writeObject(response);
                    output.flush();
                } catch (ClassNotFoundException e) {
                    System.out.println(ErrorMessages.commandExecutionError(e.getMessage()));
                } finally {
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.out.println(ErrorMessages.commandExecutionError(e.getMessage()));
        }
    }
}