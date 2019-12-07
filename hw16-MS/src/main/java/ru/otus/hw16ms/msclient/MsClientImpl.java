package ru.otus.hw16ms.msclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16common.common.Serializers;
import ru.otus.hw16common.message.Message;
import ru.otus.hw16common.messagesystem.MessageType;
import ru.otus.hw16common.messagesystem.MsClient;
import ru.otus.hw16common.messagesystem.RequestHandler;
import ru.otus.hw16ms.msocket.MsSocketClient;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MsClientImpl implements MsClient {
    private static final Logger logger = LoggerFactory.getLogger(MsClientImpl.class);

    private final String name;
    private final MsSocketClient socketClient;
    private final Map<String, RequestHandler> handlers = new ConcurrentHashMap<>();

    public MsClientImpl(String name, MsSocketClient socketClient) {
        this.name = name;
        this.socketClient = socketClient;
    }

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        this.handlers.put(type.getValue(), requestHandler);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean sendMessage(Message msg) {
        boolean result = socketClient.newMessage(msg);
        if (!result) {
            logger.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @Override
    public void handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            socketClient.newMessage(msg);
        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
    }

    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType) {
        return new Message(name, to, null, msgType.getValue(), Serializers.serialize(data));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(name, msClient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
