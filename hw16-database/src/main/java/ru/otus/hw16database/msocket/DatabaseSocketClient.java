package ru.otus.hw16database.msocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16common.msocket.BaseMSocketClient;

@Component
public class DatabaseSocketClient extends BaseMSocketClient {
    public DatabaseSocketClient(@Value("${service.ms.host}") String connectionHost, @Value("${service.ms.port}") int connectionPort) {
        super(connectionHost, connectionPort);
    }
}
