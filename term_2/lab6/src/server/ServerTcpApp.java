package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Scanner;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import network.CommandRequest;
import network.CommandResponse;
import util.JsonUtil;

public class ServerTcpApp {
    private final ServerCommandProcessor serverCommandProcessor;
    private final int port;
    private final CollectionManager collectionManager;
    private final String filename;
    private final Scanner scanner = new Scanner(System.in);


    public ServerTcpApp(ServerCommandProcessor serverCommandProcessor, int port, CollectionManager collectionManager, String filename) {
        this.serverCommandProcessor = serverCommandProcessor;
        this.port = port;
        this.collectionManager = collectionManager;
        this.filename = filename;
    }


    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(200);

            System.out.println("Сервер слушает порт " + port + ".");

            while (true) {
                if (filename != null && !filename.isBlank() && System.in.available() > 0) {
                    String line = scanner.nextLine().trim();

                    if (line.equals("save")) {
                        JsonUtil.writeToFile(filename, collectionManager.getCollection());
                        System.out.println(ErrorMessages.savedToFile(filename));
                    }
                }
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Клиент подключен.");

                    try (
                            var input = clientSocket.getInputStream();
                            var output = clientSocket.getOutputStream()
                    ) {
                        byte[] lengthBytes = input.readNBytes(Integer.BYTES);
                        if (lengthBytes.length < Integer.BYTES) {
                            continue;
                        }

                        int requestLength = ByteBuffer.wrap(lengthBytes).getInt();
                        byte[] requestBytes = input.readNBytes(requestLength);

                        if (requestBytes.length < requestLength) {
                            continue;
                        }

                        CommandRequest request = deserializeRequest(requestBytes);
                        CommandResponse response = serverCommandProcessor.process(request);

                        byte[] responseBytes = serializeResponse(response);

                        ByteBuffer responseLengthBuffer = ByteBuffer.allocate(Integer.BYTES);
                        responseLengthBuffer.putInt(responseBytes.length);
                        output.write(responseLengthBuffer.array());
                        output.write(responseBytes);
                        output.flush();

                    } catch (ClassNotFoundException e) {
                        System.out.println(ErrorMessages.commandExecutionError(e.getMessage()));
                    } finally {
                        clientSocket.close();
                    }
                              
                } catch (SocketTimeoutException e) {
                    continue;
                }
            }
        } catch (IOException e) {
            System.out.println(ErrorMessages.commandExecutionError(e.getMessage()));
        }
    }

    private CommandRequest deserializeRequest(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (CommandRequest) objectStream.readObject();
        }
    }

    private byte[] serializeResponse(CommandResponse response) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(response);
            objectStream.flush();
            return byteStream.toByteArray();

        }
    }
}