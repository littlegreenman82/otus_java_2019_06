package ru.otus.hw16frontend.msocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16common.msocket.MSocketClientImpl;

@Component
public class FrontendSocketClient extends MSocketClientImpl {
    public FrontendSocketClient(@Value("${service.ms.host}") String connectionHost, @Value("${service.ms.port}") int connectionPort) {
        super(connectionHost, connectionPort);
    }
}
