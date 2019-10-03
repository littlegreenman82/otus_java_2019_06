package ru.otus.hw10.api;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface DatabaseSession {
    Session getHibernateSession();

    Transaction getTransaction();

    void close();
}
