package ru.otus.hw16database.msocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.message.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;

@Component
public class Client {
    private final Logger logger = LoggerFactory.getLogger(Client.class);

    @Value("${service.ms.host}")
    private String connectionHost;
    @Value("${service.ms.port}")
    private int connectionPort;

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
