package ru.otus.hw10.hibernate.executor;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw10.api.DbExecutor;
import ru.otus.hw10.api.SessionManager;
import ru.otus.hw10.entity.Address;
import ru.otus.hw10.entity.Phone;
import ru.otus.hw10.entity.User;
import ru.otus.hw10.hibernate.HibernateUtils;
import ru.otus.hw10.hibernate.sessionmanager.SessionManagerHibernate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Сервис для работы с сущностями должен ")
class DbExecutorImplTest {

    private DbExecutor<User> dbExecutorUser;

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml",
                User.class,
                Address.class,
                Phone.class
        );

        SessionManager sessionManager = new SessionManagerHibernate(sessionFactory);
        dbExecutorUser = new DbExecutorImpl<>(sessionManager);
    }

    @Test
    @DisplayName("корректно создавать пользователя с зависимыми сущностями")
    void createUserWithRelatedEntities() {
        final var user = createUserTemplateObjectWithRelatedData();

        assertDoesNotThrow(() -> dbExecutorUser.create(user));
        assertNotEquals(0, user.getId());

        user.getPhones().forEach(phone -> assertNotEquals(0, phone.getId()));
    }

    @Test
    @DisplayName("корректно обновлять пользователя")
    void update() {
        final var user = createUserTemplateObjectWithRelatedData();

        assertDoesNotThrow(() -> dbExecutorUser.create(user));
        assertNotEquals(0, user.getId());


        final var oldId = user.getId();
        user.setName("JW");

        final var oldAddressId = user.getAddress().getId();
        user.getAddress().setStreet("221");

        assertDoesNotThrow(() -> dbExecutorUser.update(user));
        assertEquals(oldId, user.getId());
        assertEquals("JW", user.getName());
        assertEquals(oldAddressId, user.getAddress().getId());
    }

    @Test
    @DisplayName("корректно обновлять или создавать новую запись")
    void createOrUpdate() {
        final var user = new User("GH", null);

        assertDoesNotThrow(() -> dbExecutorUser.createOrUpdate(user));
        final var id = user.getId();
        assertNotEquals(0, id);
        user.setName("JW");

        assertDoesNotThrow(() -> dbExecutorUser.createOrUpdate(user));
        assertEquals(id, user.getId());
    }

    @Test
    @DisplayName("корректно загружать пользователя из бд")
    void load() {
        final var user = createUserTemplateObjectWithRelatedData();

        assertDoesNotThrow(() -> {
            dbExecutorUser.create(user);
            final var loadedUser = dbExecutorUser.load(user.getId(), User.class);

            assertEquals(loadedUser.getId(), user.getId());
            assertEquals(loadedUser.getName(), user.getName());
            assertEquals(loadedUser.getAddress().getStreet(), user.getAddress().getStreet());
        });
    }

    @Test
    @DisplayName("корректно удалять пользователя")
    void delete() {
        final var user = createUserTemplateObjectWithRelatedData();

        assertDoesNotThrow(() -> {
            dbExecutorUser.create(user);
            dbExecutorUser.delete(user);

            final var loadedUser = dbExecutorUser.load(user.getId(), User.class);

            assertNull(loadedUser);
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