package ru.otus;

import ru.otus.api.SessionManager;
import ru.otus.entity.Account;
import ru.otus.entity.User;
import ru.otus.jdbc.DDLExecutor;
import ru.otus.jdbc.executor.DbExecutorImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

public class Application {

    public static void main(String[] args) {

        try (SessionManager sessionManagerJdbc = new SessionManagerJdbc(new H2())) {

            final var ddlExecutor = new DDLExecutor(sessionManagerJdbc);

            ddlExecutor.createUserTable();
            ddlExecutor.createAccountTable();

            DbExecutorImpl<User> userDbExecutor = new DbExecutorImpl<>(sessionManagerJdbc);

            var user = new User("Bob", 26);
            userDbExecutor.create(user);

            final var load = userDbExecutor.load(user.getId(), User.class);

            load.setName("Rob");
            load.setAge(16);
            userDbExecutor.update(load);

            var newUser = new User("Joe", 18);
            userDbExecutor.createOrUpdate(newUser);


            var accountDbExecutor = new DbExecutorImpl<>(sessionManagerJdbc);
            var account = new Account("normal", 15F);

            accountDbExecutor.create(account);

            accountDbExecutor.load(account.getNo(), Account.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
