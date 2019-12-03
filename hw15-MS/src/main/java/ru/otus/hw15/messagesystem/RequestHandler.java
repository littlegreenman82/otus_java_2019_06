package ru.otus.hw15.messagesystem;


import ru.otus.hw15.exception.DbServiceException;

import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg) throws DbServiceException;
}
