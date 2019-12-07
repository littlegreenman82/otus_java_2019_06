package ru.otus.hw16common.messagesystem;


import ru.otus.hw16common.message.Message;

import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg) throws Exception;
}
