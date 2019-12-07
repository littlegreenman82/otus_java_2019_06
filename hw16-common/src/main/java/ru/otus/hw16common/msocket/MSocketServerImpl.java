package ru.otus.hw16common.msocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import ru.otus.hw16common.message.Message;
import ru.otus.hw16common.messagesystem.MsClient;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class MSocketServerImpl implements MSocketServer {
    private final Logger logger = LoggerFactory.getLogger(MSocketServerImpl.class);

    private int connectionPort;
    private MsClient msClient;

    public MSocketServerImpl(
            MsClient msClient,
            @Value("${service.db.port}") int connectionPort) {
        this.msClient = msClient;
        this.connectionPort = connectionPort;
    }

    @Override
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

    @SuppressWarnings("WeakerAccess")
    protected void clientHandler(Socket clientSocket) {
        try (final var objectInputStream = new ObjectInputStream(clientSocket.getInputStream())) {
            final var message = (Message) objectInputStream.readObject();

            msClient.handle(message);

            logger.info("message received {}", message);
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }
}
