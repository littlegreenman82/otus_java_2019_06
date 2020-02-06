package ru.otus.hw16ms.msocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16common.message.Message;
import ru.otus.hw16common.messagesystem.MessageSystem;
import ru.otus.hw16common.msocket.MSocketServer;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class MsSocketServer implements MSocketServer {
    private final Logger logger = LoggerFactory.getLogger(MsSocketServer.class);
    
    @Value("${service.ms.host}")
    private String connectionServer;
    @Value("${service.ms.port}")
    private int connectionPort;
    
    private final MessageSystem messageSystem;
    
    @Autowired
    public MsSocketServer(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }
    
    public void serve() {
        try (var serverSocket = new ServerSocket(connectionPort)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket);
                }
            }
        } catch (Exception e) {
            logger.error("error", e);
        }
    }
    
    private void clientHandler(Socket clientSocket) {
        try (final var objectInputStream = new ObjectInputStream(clientSocket.getInputStream())) {
            final var message = (Message) objectInputStream.readObject();
            messageSystem.newMessage(message);
            logger.info("receive message: {}", message);
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
