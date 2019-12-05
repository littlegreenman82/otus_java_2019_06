package ru.otus.hw16frontend.msocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16frontend.front.handlers.GetUserDataResponseHandler;
import ru.otus.message.Message;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Server {
    private final Logger logger = LoggerFactory.getLogger(Client.class);
    
    @Value("${service.frontend.host}")
    private String connectionServer;
    @Value("${service.frontend.port}")
    private int connectionPort;
    private GetUserDataResponseHandler userDataResponseHandler;
    
    @Autowired
    public Server(GetUserDataResponseHandler userDataResponseHandler) {
        this.userDataResponseHandler = userDataResponseHandler;
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
            userDataResponseHandler.handle(message);
            logger.info("message received {}", message);
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
