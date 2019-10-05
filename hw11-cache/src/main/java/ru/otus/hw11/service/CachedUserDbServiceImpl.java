package ru.otus.hw11.service;

import ru.otus.hw11.cache.HwCacheImpl;
import ru.otus.hw11.dao.UserDao;
import ru.otus.hw11.entity.User;
import ru.otus.hw11.hibernate.exception.DbServiceException;

import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public class CachedUserDbServiceImpl extends UserDbServiceImpl {
    private final HwCacheImpl<Long, User> cache;

    public CachedUserDbServiceImpl(UserDao userDao) {
        super(userDao);
        this.cache = new HwCacheImpl<>();
    }

    @Override
    public Optional<User> get(long id) throws DbServiceException {
        final var cachedUser = cache.get(id);
        if (cachedUser != null) {
            return Optional.of(cachedUser);
        }

        var userOptional = super.get(id);
        userOptional.ifPresent(user -> cache.put(id, user));

        return userOptional;
    }
}
