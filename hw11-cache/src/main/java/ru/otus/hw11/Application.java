package ru.otus.hw11;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw11.api.SessionManager;
import ru.otus.hw11.dao.UserDao;
import ru.otus.hw11.entity.Address;
import ru.otus.hw11.entity.Phone;
import ru.otus.hw11.entity.User;
import ru.otus.hw11.hibernate.HibernateUtils;
import ru.otus.hw11.hibernate.exception.DbServiceException;
import ru.otus.hw11.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.hw11.service.UserDbServiceImpl;

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
        final var userDao = new UserDao(sessionManager);
        final var userDbService = new UserDbServiceImpl(userDao);

        final var phone = new Phone("+76669991984");
        final var addressDataSet = new Address("Elm");

        final var freddy = new User("Freddy", addressDataSet);
        freddy.getPhones().add(phone);
        freddy.getPhones().forEach(phone1 -> phone1.setUser(freddy));

        try {
            userDbService.save(freddy);

            final var load = userDbService.get(freddy.getId());
            if (load.isPresent()) {
                userDbService.delete(load.get());
            }
        } catch (DbServiceException e) {
            logger.error(e.getMessage());
        }
    }
}
