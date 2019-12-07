package ru.otus.hw16database.msclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16common.messagesystem.BaseMsClient;
import ru.otus.hw16common.msocket.MSocketClient;

@Component
public class DatabaseMsClient extends BaseMsClient {
    public DatabaseMsClient(@Value("${service.db.name}") String name, MSocketClient databaseSocketClient) {
        super(name, databaseSocketClient);
    }
}
