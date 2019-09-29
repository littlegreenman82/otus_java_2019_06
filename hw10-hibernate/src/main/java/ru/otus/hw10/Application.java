package ru.otus.hw10;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw10.api.DbExecutor;
import ru.otus.hw10.api.SessionManager;
import ru.otus.hw10.entity.Address;
import ru.otus.hw10.entity.Phone;
import ru.otus.hw10.entity.User;
import ru.otus.hw10.hibernate.HibernateUtils;
import ru.otus.hw10.hibernate.executor.DbExecutorImpl;
import ru.otus.hw10.hibernate.sessionmanager.SessionManagerHibernate;

public class Application {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Application.class);

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml",
                User.class,
                Address.class,
                Phone.class
        );

        SessionManager sessionManager = new SessionManagerHibernate(sessionFactory);
        DbExecutor<User> dbExecutorUser = new DbExecutorImpl<>(sessionManager);

        final var phone = new Phone("+76669991984");
        final var addressDataSet = new Address("Elm");

        final var freddy = new User("Freddy", addressDataSet);
        freddy.getPhones().add(phone);
        freddy.getPhones().forEach(phone1 -> phone1.setUser(freddy));

        try {
            dbExecutorUser.create(freddy);

            final var load = dbExecutorUser.load(freddy.getId(), User.class);
            logger.info("Loaded object: {}", load);

            dbExecutorUser.delete(load);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
