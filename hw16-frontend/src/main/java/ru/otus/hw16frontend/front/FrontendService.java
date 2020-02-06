package ru.otus.hw16frontend.front;


import ru.otus.hw16frontend.dto.UserDTO;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {
    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);

    void saveUser(String user, Consumer<String> dataConsumer);
}

