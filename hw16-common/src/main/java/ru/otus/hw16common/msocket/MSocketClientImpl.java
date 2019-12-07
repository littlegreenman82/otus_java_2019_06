package ru.otus.hw16common.msocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16common.message.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class MSocketClientImpl implements MSocketClient {
    private final Logger logger = LoggerFactory.getLogger(MSocketClientImpl.class);

    private String connectionHost;

    private int connectionPort;

    public MSocketClientImpl(String connectionHost, int connectionPort) {
        this.connectionHost = connectionHost;
        this.connectionPort = connectionPort;
    }

    @Override
    public boolean newMessage(Message message) {
        try {
            try (var socket = new Socket(connectionHost, connectionPort)) {
                final var outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(message);
                logger.info("message send {}", message);
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
