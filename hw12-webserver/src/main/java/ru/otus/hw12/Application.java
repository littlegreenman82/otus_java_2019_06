package ru.otus.hw12;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw12.dao.UserDao;
import ru.otus.hw12.entity.Address;
import ru.otus.hw12.entity.Phone;
import ru.otus.hw12.entity.User;
import ru.otus.hw12.filter.AuthorizationFilter;
import ru.otus.hw12.hibernate.HibernateUtils;
import ru.otus.hw12.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.hw12.service.UserDbServiceImpl;
import ru.otus.hw12.servlet.AuthServlet;
import ru.otus.hw12.servlet.IndexServlet;
import ru.otus.hw12.servlet.UsersServlet;

@SuppressWarnings("ALL")
public class Application {
    private static final int PORT = 8080;
    private static final String STATIC = "/static";
    private final SessionManagerHibernate sessionManager;
    private final Logger logger;

    public Application(SessionFactory sessionFactory) {
        this.logger = LoggerFactory.getLogger(Application.class);
        this.sessionManager = new SessionManagerHibernate(sessionFactory);
    }

    public static void main(String[] args) {
        var sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml",
                User.class,
                Address.class,
                Phone.class
        );

        new Application(sessionFactory).serve();
    }

    public void serve() {
        var userDao = new UserDao(sessionManager);
        var userDbService = new UserDbServiceImpl(userDao);

        var servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(userDbService)), "/api/users");
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(userDbService)), "/api/users/add");
        servletContextHandler.addServlet(new ServletHolder(new AuthServlet()), "/api/login");
        servletContextHandler.addServlet(new ServletHolder(new IndexServlet()), "/");

        servletContextHandler.addFilter(new FilterHolder(new AuthorizationFilter()), "/api/users", null);
        servletContextHandler.addFilter(new FilterHolder(new AuthorizationFilter()), "/api/users/*", null);

        var resourceHandler = new ResourceHandler();
        var resource = Resource.newClassPathResource(STATIC);
        resourceHandler.setBaseResource(resource);

        var server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, servletContextHandler));

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            server.destroy();
        }
    }
}
