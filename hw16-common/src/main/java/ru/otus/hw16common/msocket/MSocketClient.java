package ru.otus.hw16common.msocket;

import ru.otus.hw16common.message.Message;

public interface MSocketClient {
    boolean newMessage(Message message);
}
