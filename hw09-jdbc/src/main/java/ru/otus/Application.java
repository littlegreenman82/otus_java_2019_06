package ru.otus;

import ru.otus.entity.Account;
import ru.otus.entity.User;
import ru.otus.jdbc.executor.DbExecutorImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

public class Application {

    public static void main(String[] args) {
        SessionManagerJdbc sessionManagerJdbc = new SessionManagerJdbc(new H2());
    
        DbExecutorImpl.createUserTable(sessionManagerJdbc);
        DbExecutorImpl.createAccountTable(sessionManagerJdbc);
        
        DbExecutorImpl<User> userDbExecutor = new DbExecutorImpl<>(sessionManagerJdbc);

        try {
//            User user = new User();
//            user.setName("Bob");
//            user.setAge(25);
//            userDbExecutor.create(user);
//
//            final User load = userDbExecutor.load(user.getId(), User.class);
//
//            load.setName("Rob");
//            load.setAge(16);
//            userDbExecutor.update(load);
//
//            var newUser = new User();
//            newUser.setName("Joe");
//            newUser.setAge(18);
//            userDbExecutor.createOrUpdate(newUser);
//
//
            var accountDbExecutor = new DbExecutorImpl<>(sessionManagerJdbc);
            var account           = new Account();
    
            account.setType("normal");
            account.setRest(15F);
    
            accountDbExecutor.create(account);
    
            accountDbExecutor.load(account.getNo(), Account.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        sessionManagerJdbc.close();
    }
}
