package ru.otus.hw16frontend.msocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16common.messagesystem.MessageType;
import ru.otus.hw16common.messagesystem.MsClient;
import ru.otus.hw16common.msocket.BaseMSocketServer;
import ru.otus.hw16frontend.front.handlers.GetUserDataResponseHandler;

@Component
public class FrontendSocketServer extends BaseMSocketServer {

    @Autowired
    public FrontendSocketServer(MsClient msClient,
                                @Value("${service.frontend.port}") int connectionPort,
                                GetUserDataResponseHandler responseHandler) {
        super(msClient, connectionPort);
        msClient.addHandler(MessageType.SAVE_USER, responseHandler);
    }
}
