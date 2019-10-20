package ru.otus.hw12.api;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface DatabaseSession {
    Session getHibernateSession();

    Transaction getTransaction();

    void close();
}
