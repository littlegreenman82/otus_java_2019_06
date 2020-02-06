package ru.otus.hw16ms.msclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16common.message.Message;
import ru.otus.hw16common.messagesystem.BaseMsClient;
import ru.otus.hw16ms.msocket.MsSocketClient;

public class MsClientImpl extends BaseMsClient {
    private static final Logger logger = LoggerFactory.getLogger(MsClientImpl.class);

    public MsClientImpl(String name, MsSocketClient socketClient) {
        super(name, socketClient);
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
}
