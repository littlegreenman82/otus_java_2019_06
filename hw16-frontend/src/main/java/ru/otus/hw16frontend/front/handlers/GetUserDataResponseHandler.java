package ru.otus.hw16frontend.front.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw16common.common.Serializers;
import ru.otus.hw16common.message.Message;
import ru.otus.hw16common.messagesystem.RequestHandler;
import ru.otus.hw16frontend.front.FrontendService;

import java.util.Optional;
import java.util.UUID;

@Component
public class GetUserDataResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserDataResponseHandler.class);

    private final FrontendService frontendService;

    @Autowired
    public GetUserDataResponseHandler(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            String userJson = Serializers.deserialize(msg.getPayload(), String.class);
            UUID sourceMessageId = msg.getSourceMessageId();
            if (sourceMessageId == null) {
                throw new RuntimeException("Not found sourceMsg for message:" + msg.getId());
            }
            frontendService.takeConsumer(sourceMessageId, String.class).ifPresent(consumer -> consumer.accept(userJson));
        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
