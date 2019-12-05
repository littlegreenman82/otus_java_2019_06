package ru.otus.hw16database.messagesystem;

import ru.otus.message.Message;

public interface MessageSystem {

    void addClient(MsClient msClient);

    void removeClient(String clientId);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;
}

