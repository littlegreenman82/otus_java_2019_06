package ru.otus.hw16database.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.hw16common.common.Serializers;
import ru.otus.hw16common.message.Message;
import ru.otus.hw16common.messagesystem.MessageType;
import ru.otus.hw16common.messagesystem.RequestHandler;
import ru.otus.hw16database.entity.User;
import ru.otus.hw16database.exception.DbServiceException;

import java.util.Optional;

@Component
public class GetUserDataRequestHandler implements RequestHandler {
    private Logger logger = LoggerFactory.getLogger(GetUserDataRequestHandler.class);
    
    private final UserService userService;

    public GetUserDataRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Message> handle(Message msg) throws DbServiceException, JsonProcessingException {
        String userJson = Serializers.deserialize(msg.getPayload(), String.class);
    
        final var objectMapper = new ObjectMapper();
        final var user = objectMapper.readValue(userJson, User.class);
        userService.save(user);
        
        userJson = objectMapper.writeValueAsString(user);
        logger.info("user saved {}", user);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(),
                MessageType.SAVE_USER.getValue(), Serializers.serialize(userJson)));
    }
}
