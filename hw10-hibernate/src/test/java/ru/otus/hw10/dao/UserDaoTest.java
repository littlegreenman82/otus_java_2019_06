package ru.otus.hw10.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw10.api.SessionManager;
import ru.otus.hw10.entity.Address;
import ru.otus.hw10.entity.Phone;
import ru.otus.hw10.entity.User;
import ru.otus.hw10.hibernate.HibernateUtils;
import ru.otus.hw10.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    private UserDao userDao;

    private Logger logger = LoggerFactory.getLogger(UserDaoTest.class);

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml",
                User.class,
                Address.class,
                Phone.class
        );

        SessionManager sessionManager = new SessionManagerHibernate(sessionFactory);
        this.userDao = new UserDao(sessionManager);
    }

    @Test
    @DisplayName("корректно создавать пользователя с зависимыми сущностями")
    void saveUserWithRelatedEntities() {
        final var user = createUserTemplateObjectWithRelatedData();

        assertDoesNotThrow(() -> userDao.save(user));
        assertNotEquals(0, user.getId());

        user.getPhones().forEach(phone -> assertNotEquals(0, phone.getId()));
    }

    @Test
    @DisplayName("корректно обновлять пользователя")
    void update() {
        final var user = createUserTemplateObjectWithRelatedData();

        assertDoesNotThrow(() -> userDao.save(user));
        assertNotEquals(0, user.getId());

        final var oldId = user.getId();
        user.setName("JW");

        final var oldAddressId = user.getAddress().getId();
        user.getAddress().setStreet("221");

        assertDoesNotThrow(() -> userDao.update(user));
        assertEquals(oldId, user.getId());
        assertEquals("JW", user.getName());
        assertEquals(oldAddressId, user.getAddress().getId());
    }

    @Test
    @DisplayName("корректно загружать пользователя из бд")
    void load() {
        final var user = createUserTemplateObjectWithRelatedData();

        assertDoesNotThrow(() -> {
            userDao.save(user);
            final Optional<User> loadedUserOptional = userDao.get(user.getId());

            loadedUserOptional.ifPresent((loadedUser) -> {
                assertEquals(loadedUser.getId(), user.getId());
                assertEquals(loadedUser.getName(), user.getName());
                assertEquals(loadedUser.getAddress().getStreet(), user.getAddress().getStreet());
            });
        });
    }

    @Test
    @DisplayName("корректно загружать пользователя из бд с ленивой загрузкой связанных данных")
    void loadWithLazyCollection() {
        final var user = createUserTemplateObjectWithRelatedData();

        assertDoesNotThrow(() -> {
            userDao.save(user);
            final Optional<User> loadedUserOptional = userDao.get(user.getId());

            loadedUserOptional.ifPresent((loadedUser) -> {
                final var phones = loadedUser.getPhones();

                logger.info("Fetched lazy loaded collection: {}", phones);
            });
        });
    }

    @Test
    @DisplayName("корректно удалять пользователя")
    void delete() {
        final var user = createUserTemplateObjectWithRelatedData();

        assertDoesNotThrow(() -> {
            userDao.save(user);
            userDao.delete(user);

            final var loadedUser = userDao.get(user.getId());

            assertTrue(loadedUser.isEmpty());
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