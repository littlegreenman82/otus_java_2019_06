package ru.otus.hw16database.msocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16database.messagesystem.MessageType;
import ru.otus.hw16database.messagesystem.MsClient;
import ru.otus.hw16database.service.GetUserDataRequestHandler;
import ru.otus.message.Message;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Server {
    private final Logger logger = LoggerFactory.getLogger(Client.class);
    
    @Value("${service.db.host}")
    private String connectionServer;
    @Value("${service.db.port}")
    private int connectionPort;
    private MsClient msClient;
    
    @Autowired
    public Server(MsClient msClient, GetUserDataRequestHandler userDataRequestHandler) {
        this.msClient = msClient;
        msClient.addHandler(MessageType.SAVE_USER, userDataRequestHandler);
    }
    
    public void serve() {
        try (var serverSocket = new ServerSocket(connectionPort)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    logger.info("client connected");
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
            
            msClient.handle(message);
            
            logger.info("message received {}", message);
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
