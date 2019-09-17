package ru.otus;

import ru.otus.entity.User;
import ru.otus.jdbc.executor.DbExecutorImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) {
        SessionManagerJdbc sessionManagerJdbc = new SessionManagerJdbc(new H2());
        sessionManagerJdbc.beginSession();

        try (PreparedStatement pst = sessionManagerJdbc.getCurrentSession()
                .getConnection()
                .prepareStatement("create table user(id long auto_increment, name varchar(50), age int)")) {

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DbExecutorImpl<User> userDbExecutor = new DbExecutorImpl<>(sessionManagerJdbc);

        try {
            User user = new User();
            user.setAge(25);
            user.setName("Hrist");
            userDbExecutor.create(user);

            System.out.println(user.getId());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        sessionManagerJdbc.commitSession();
        sessionManagerJdbc.close();
    }
}
