package ru.otus.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SessionManager;
import ru.otus.jdbc.exception.DbExecutorException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("SqlResolve")
public class DDLExecutor {

    private final SessionManager sessionManagerJdbc;
    private Logger logger = LoggerFactory.getLogger(DDLExecutor.class);

    public DDLExecutor(SessionManager sessionManager) {
        this.sessionManagerJdbc = sessionManager;
    }

    public void createUserTable() throws DbExecutorException {
        sessionManagerJdbc.beginSession();

        try (PreparedStatement pst = sessionManagerJdbc.getCurrentSession()
                .getConnection()
                .prepareStatement("create table user(id long auto_increment, name varchar(50), age int)")) {

            pst.executeUpdate();
            sessionManagerJdbc.commitSession();
            logger.info("Table 'user' was created!");
        } catch (SQLException e) {
            logger.info("Error creating table: {}", e.getMessage());
            sessionManagerJdbc.rollbackSession();
            throw new DbExecutorException();
        }
    }

    public void createAccountTable() throws DbExecutorException {
        sessionManagerJdbc.beginSession();

        try (PreparedStatement pst = sessionManagerJdbc.getCurrentSession()
                .getConnection()
                .prepareStatement("create table account(no long auto_increment, type varchar(255), rest number)")) {

            pst.executeUpdate();
            sessionManagerJdbc.commitSession();
            logger.info("Table 'account' was created!");
        } catch (SQLException e) {
            logger.info("Error creating table: {}", e.getMessage());
            sessionManagerJdbc.rollbackSession();
            throw new DbExecutorException();
        }
    }

    public void deleteUserTable() throws DbExecutorException {
        sessionManagerJdbc.beginSession();


        try (PreparedStatement pst = sessionManagerJdbc.getCurrentSession()
                .getConnection()
                .prepareStatement("drop table user")) {

            pst.executeUpdate();
            sessionManagerJdbc.commitSession();
            logger.info("Table 'user' was dropped!");
        } catch (SQLException e) {
            logger.info("Error deleting table: {}", e.getMessage());
            sessionManagerJdbc.rollbackSession();
            throw new DbExecutorException();
        }
    }

    public void deleteAccountTable() throws DbExecutorException {
        sessionManagerJdbc.beginSession();

        try (PreparedStatement pst = sessionManagerJdbc.getCurrentSession()
                .getConnection()
                .prepareStatement("drop table account")) {

            pst.executeUpdate();
            sessionManagerJdbc.commitSession();
            logger.info("Table 'account' was dropped!");
        } catch (SQLException e) {
            logger.info("Error deleting table: {}", e.getMessage());
            sessionManagerJdbc.rollbackSession();
            throw new DbExecutorException();
        }
    }
}
