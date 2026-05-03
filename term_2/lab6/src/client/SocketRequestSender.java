package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import exceptions.ErrorMessages;
import network.CommandRequest;
import network.CommandResponse;


public class SocketRequestSender implements RequestSender {
    private final String host;
    private final int port;

    public SocketRequestSender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public CommandResponse send(CommandRequest request) {
        try (SocketChannel channel = SocketChannel.open()) {
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host, port));

            while (!channel.finishConnect()) {
                Thread.onSpinWait();
            }

            byte[] requestBytes = serialize(request);

            ByteBuffer lengthBuffer = ByteBuffer.allocate(Integer.BYTES);
            lengthBuffer.putInt(requestBytes.length);
            lengthBuffer.flip();
            while (lengthBuffer.hasRemaining()) {
                channel.write(lengthBuffer);
            }

            ByteBuffer dataBuffer = ByteBuffer.wrap(requestBytes);
            while (dataBuffer.hasRemaining()) {
                channel.write(dataBuffer);
            }

            ByteBuffer responseLengthBuffer = ByteBuffer.allocate(Integer.BYTES);
            while (responseLengthBuffer.hasRemaining()) {
                if (channel.read(responseLengthBuffer) == -1) {
                    throw new IOException("Сервер закрыл соединение.");
                }
            }
            responseLengthBuffer.flip();
            int responseLength = responseLengthBuffer.getInt();

            ByteBuffer responseDataBuffer = ByteBuffer.allocate(responseLength);
            while (responseDataBuffer.hasRemaining()) {
                if (channel.read(responseDataBuffer) == -1) {
                    throw new IOException("Сервер закрыл соединение.");
                }
            }

            return deserialize(responseDataBuffer.array());
        } catch (IOException | ClassNotFoundException e) {
            return new CommandResponse(false, ErrorMessages.serverUnavailable(e.getMessage()), null);
        }
    }

    private byte[] serialize(CommandRequest request) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(request);
            objectStream.flush();
            return byteStream.toByteArray();
        }
    }

    private CommandResponse deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (CommandResponse) objectStream.readObject();
        }
    }
}