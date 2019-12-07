package ru.otus.hw16ms.msocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.hw16common.message.Message;
import ru.otus.hw16common.msocket.MSocketClient;

import java.io.ObjectOutputStream;
import java.net.Socket;

@Component
@Scope("prototype")
public class MsSocketClient implements MSocketClient {
    private final Logger logger = LoggerFactory.getLogger(MsSocketClient.class);

    private String connectionHost;
    private int connectionPort;

    public boolean newMessage(Message message) {
        try {
            try (var socket = new Socket(connectionHost, connectionPort)) {
                final var outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(message);
                logger.info("send message from ms {} {} {}", message, connectionHost, connectionPort);
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
    
    public void setConnectionHost(String connectionServer) {
        this.connectionHost = connectionServer;
    }
    
    public void setConnectionPort(int connectionPort) {
        this.connectionPort = connectionPort;
    }
}
