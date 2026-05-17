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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

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
            ExecutorService requestExecutor = Executors.newCachedThreadPool();
            ForkJoinPool responseExecutor = new ForkJoinPool();

            serverSocket.setSoTimeout(200);

            System.out.println("Сервер слушает порт " + port + ".");

            Thread readerThread = new Thread(() -> {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Клиент подключен.");

                        byte[] requestBytes;

                        InputStream input = clientSocket.getInputStream();
                        byte[] lengthBytes = input.readNBytes(Integer.BYTES);
                        if (lengthBytes.length < Integer.BYTES) {
                            clientSocket.close();
                            continue;
                        }

                        int requestLength = ByteBuffer.wrap(lengthBytes).getInt();
                        requestBytes = input.readNBytes(requestLength);

                        if (requestBytes.length < requestLength) {
                            clientSocket.close();
                            continue;
                        }              
                        
                        
                        CommandRequest request = deserializeRequest(requestBytes);

                        requestExecutor.submit(() -> {
                            try {
                                CommandResponse response = serverCommandProcessor.process(request);

                                responseExecutor.execute(() -> {
                                    try {
                                        byte[] responseBytes = serializeResponse(response);

                                        OutputStream output = clientSocket.getOutputStream();

                                        ByteBuffer responseLengthBuffer = ByteBuffer.allocate(Integer.BYTES);
                                        responseLengthBuffer.putInt(responseBytes.length);

                                        output.write(responseLengthBuffer.array());
                                        output.write(responseBytes);
                                        output.flush();
                                    } catch (IOException e) {
                                        System.out.println(ErrorMessages.commandExecutionError(e.getMessage()));
                                    } finally {
                                        try {
                                            clientSocket.close();
                                        } catch (IOException e) {
                                            System.out.println(ErrorMessages.commandExecutionError(e.getMessage()));
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                System.out.println(ErrorMessages.commandExecutionError(e.getMessage()));
                                try {
                                    clientSocket.close();
                                } catch (IOException ioException) {
                                    System.out.println(ErrorMessages.commandExecutionError(ioException.getMessage()));
                                }
                            }
                        });
                        
                    } catch (SocketTimeoutException e) {
                        continue;
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println(ErrorMessages.commandExecutionError(e.getMessage()));
                    }
                }
            });
            readerThread.start();
            readerThread.join();
        } catch (IOException | InterruptedException e) {
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