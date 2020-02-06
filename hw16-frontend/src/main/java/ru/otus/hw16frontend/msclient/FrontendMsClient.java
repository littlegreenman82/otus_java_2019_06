package ru.otus.hw16frontend.msclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16common.messagesystem.BaseMsClient;
import ru.otus.hw16frontend.msocket.FrontendSocketClient;

@Component
public class FrontendMsClient extends BaseMsClient {
    public FrontendMsClient(@Value("${service.frontend.name}") String name, FrontendSocketClient socketClient) {
        super(name, socketClient);
    }
}
