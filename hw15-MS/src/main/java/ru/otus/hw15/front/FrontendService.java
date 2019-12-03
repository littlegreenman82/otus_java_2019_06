package ru.otus.hw15.front;


import ru.otus.hw15.entity.User;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {
    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);

    void saveUser(User user, Consumer<User> dataConsumer);
}

