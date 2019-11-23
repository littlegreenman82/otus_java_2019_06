package ru.otus.hw15.service;

import ru.otus.hw15.common.Serializers;
import ru.otus.hw15.entity.User;
import ru.otus.hw15.exception.DbServiceException;
import ru.otus.hw15.messagesystem.Message;
import ru.otus.hw15.messagesystem.MessageType;
import ru.otus.hw15.messagesystem.RequestHandler;

import java.util.Optional;

public class GetUserDataRequestHandler implements RequestHandler {

    private final UserService userService;

    public GetUserDataRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Message> handle(Message msg) throws DbServiceException {
        User user = Serializers.deserialize(msg.getPayload(), User.class);
        userService.save(user);

        return Optional.of(new Message(msg.getTo(), msg.getFrom(), Optional.of(msg.getId()),
                MessageType.SAVE_USER.getValue(), Serializers.serialize(user)));
    }
}
