package ru.otus.jdbc.executor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.H2;
import ru.otus.api.SessionManager;
import ru.otus.entity.Account;
import ru.otus.entity.User;
import ru.otus.jdbc.DDLExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Сервис для работы с сущностями должен ")
class DbExecutorImplTest {
    
    private SessionManager sessionManager;
    private DDLExecutor ddlExecutor;
    
    @BeforeEach
    void setUp() {
        sessionManager = new SessionManagerJdbc(new H2());
        ddlExecutor = new DDLExecutor(sessionManager);

        assertDoesNotThrow(() -> {
            ddlExecutor.createUserTable();
            ddlExecutor.createAccountTable();
        });
    }
    
    @AfterEach
    void tearDown() {
        assertDoesNotThrow(() -> {
            ddlExecutor.deleteUserTable();
            ddlExecutor.deleteAccountTable();
        });
        sessionManager.close();
    }
    
    @Test
    @DisplayName("корректно создавать нового пользователя")
    void createUser() {
        final var userDbExecutor = new DbExecutorImpl<User>(sessionManager);
        final var user = new User("Ruslan", 25);

        assertDoesNotThrow(() -> userDbExecutor.create(user));
        assertNotEquals(0, user.getId());
    }
    
    @Test
    @DisplayName("корректно создавать аккаунт")
    void createAccount() {
        final var accountDbExecutor = new DbExecutorImpl<Account>(sessionManager);
        final var account = new Account("Normal", 25F);
        
        assertDoesNotThrow(() -> accountDbExecutor.create(account));
        assertNotEquals(0, account.getNo());
    }
    
    @Test
    @DisplayName("корректно обновлять пользователя")
    void updateUser() {
        final var userDbExecutor = new DbExecutorImpl<User>(sessionManager);
        final var user = new User("Ruslan", 25);

        assertDoesNotThrow(() -> userDbExecutor.create(user));
        assertNotEquals(0, user.getId());
        
        user.setName("Oleg");
        assertDoesNotThrow(() -> userDbExecutor.update(user));
    }
    
    @Test
    @DisplayName("корректно обновлять пользователя")
    void updateAccount() {
        final var accountDbExecutor = new DbExecutorImpl<Account>(sessionManager);
        final var account = new Account("Normal", 25F);

        assertDoesNotThrow(() -> accountDbExecutor.create(account));
        assertNotEquals(0, account.getNo());
        
        account.setType("Easy");
        account.setRest(200F);
        assertDoesNotThrow(() -> accountDbExecutor.update(account));
    }
    
    @Test
    @DisplayName("создавать или обновлять пользователя")
    void createOrUpdateUser() {
        final var userDbExecutor = new DbExecutorImpl<User>(sessionManager);
        final var user = new User("Ruslan", 25);
        
        assertDoesNotThrow(() -> userDbExecutor.createOrUpdate(user));
        assertNotEquals(0, user.getId());
        
        user.setName("Oleg");
        final var lastId = user.getId();
        assertDoesNotThrow(() -> userDbExecutor.createOrUpdate(user));
        assertEquals(lastId, user.getId());
    }
    
    @Test
    @DisplayName("создавать или обновлять аккаунт")
    void createOrUpdateAccount() {
        final var accountDbExecutor = new DbExecutorImpl<Account>(sessionManager);
        final var account = new Account("Type", 25F);

        assertDoesNotThrow(() -> accountDbExecutor.createOrUpdate(account));
        assertNotEquals(0, account.getNo());
        
        account.setType("Epyt");
        final var lastId = account.getNo();
        assertDoesNotThrow(() -> accountDbExecutor.createOrUpdate(account));
        assertEquals(lastId, account.getNo());
    }
    
    @Test
    @DisplayName("загружать пользователя по id")
    void loadUser() {
        final var userDbExecutor = new DbExecutorImpl<User>(sessionManager);
        final var user = new User("Ruslan", 25);

        assertDoesNotThrow(() -> userDbExecutor.create(user));
        assertNotEquals(0, user.getId());
        
        try {
            final var loadedUser = userDbExecutor.load(user.getId(), User.class);
            
            assertEquals(user.getName(), loadedUser.getName());
            assertEquals(user.getAge(), loadedUser.getAge());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("загружать аккаунт по id")
    void loadAccount() {
        final var accountDbExecutor = new DbExecutorImpl<Account>(sessionManager);
        final var account = new Account("Type", 25F);

        assertDoesNotThrow(() -> accountDbExecutor.create(account));
        assertNotEquals(0, account.getNo());
        
        assertDoesNotThrow(() -> {
            final var loadedAccount = accountDbExecutor.load(account.getNo(), Account.class);
            
            assertEquals(account.getRest(), loadedAccount.getRest());
            assertEquals(account.getType(), loadedAccount.getType());
        });
    }
}