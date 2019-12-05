package ru.otus.hw16database.messagesystem;


import com.fasterxml.jackson.core.JsonProcessingException;
import ru.otus.hw16database.exception.DbServiceException;
import ru.otus.message.Message;

import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg) throws DbServiceException, JsonProcessingException;
}
