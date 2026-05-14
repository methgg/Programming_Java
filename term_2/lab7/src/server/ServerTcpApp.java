package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

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
            serverSocket.setSoTimeout(200);

            System.out.println("Сервер слушает порт " + port + ".");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Клиент подключен.");

                    try (
                            InputStream input = clientSocket.getInputStream();
                            OutputStream output = clientSocket.getOutputStream()
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