package ru.otus.jdbc.sessionmanager;

import ru.otus.api.DatabaseSession;

import java.sql.Connection;

@SuppressWarnings("WeakerAccess")
public class DatabaseSessionJdbc implements DatabaseSession {
    private final Connection connection;

    public DatabaseSessionJdbc(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
