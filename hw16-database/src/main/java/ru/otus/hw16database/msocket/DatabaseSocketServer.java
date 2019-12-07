package ru.otus.hw16database.msocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16common.messagesystem.MessageType;
import ru.otus.hw16common.messagesystem.MsClient;
import ru.otus.hw16common.msocket.MSocketServerImpl;
import ru.otus.hw16database.service.GetUserDataRequestHandler;

@Component
public class DatabaseSocketServer extends MSocketServerImpl {
    @Autowired
    public DatabaseSocketServer(MsClient msClient,
                                @Value("${service.db.port}") int connectionPort,
                                GetUserDataRequestHandler requestHandler) {
        super(msClient, connectionPort);
        msClient.addHandler(MessageType.SAVE_USER, requestHandler);
    }
}
