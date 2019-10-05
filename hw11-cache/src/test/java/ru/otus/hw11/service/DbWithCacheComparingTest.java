package ru.otus.hw11.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.otus.hw11.TimingExtension;
import ru.otus.hw11.api.SessionManager;
import ru.otus.hw11.api.UserDbService;
import ru.otus.hw11.dao.UserDao;
import ru.otus.hw11.entity.Address;
import ru.otus.hw11.entity.Phone;
import ru.otus.hw11.entity.User;
import ru.otus.hw11.hibernate.HibernateUtils;
import ru.otus.hw11.hibernate.exception.DbServiceException;
import ru.otus.hw11.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

// Подключим запись в лог время выполнения каждого теста для наглядности
// Смотреть в консоле
@DisplayName("Проверям что")
@ExtendWith(TimingExtension.class)
class DbWithCacheComparingTest {
    private UserDbService userDbService;
    private UserDbService cachedUserDbService;
    private List<User> users = new ArrayList<>();

    @BeforeEach
    void setUp() throws DbServiceException {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml",
                User.class,
                Address.class,
                Phone.class
        );
        SessionManager sessionManager = new SessionManagerHibernate(sessionFactory);
        final var userDao = new UserDao(sessionManager);
        this.userDbService = new UserDbServiceImpl(userDao);
        this.cachedUserDbService = new CachedUserDbServiceImpl(userDao);

        for (int i = 0; i < 200; i++) {
            var user = createUserTemplateObjectWithRelatedData();
            userDbService.save(user);
            users.add(user);
        }
    }

    @Test
    @DisplayName("сервис без кэширования работает корректно")
    void userDbServiceTest() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                for (User user : users) {
                    userDbService.get(user.getId());
                }
            }
        });
    }

    @Test
    @DisplayName("сервис с кэшированием работает корректно")
    void cachedUserDbServiceTest() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                for (User user : users) {
                    cachedUserDbService.get(user.getId());
                }
            }
        });
    }

    private User createUserTemplateObjectWithRelatedData() {
        final var address = new Address("Baker");
        final var phone1 = new Phone("9379992");
        final var phone2 = new Phone("2147483647");
        final var user = new User("SH", address);

        user.getPhones().add(phone1);
        user.getPhones().add(phone2);
        user.getPhones().forEach(phone -> phone.setUser(user));

        return user;
    }
}