package ru.otus.hw12.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw12.api.UserDbService;
import ru.otus.hw12.entity.User;
import ru.otus.hw12.hibernate.exception.DbServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsersServlet extends BaseServlet {
    private final Logger logger;

    private final UserDbService userDbService;

    public UsersServlet(UserDbService userDbService) {
        super();
        this.userDbService = userDbService;
        this.logger = LoggerFactory.getLogger(UsersServlet.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var users = userDbService.findAll();
            this.json(resp, users);
        } catch (DbServiceException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var name = req.getParameter("name");
        var user = new User();
        user.setName(name);

        try {
            userDbService.save(user);
            this.json(resp, user);
        } catch (DbServiceException e) {
            logger.error(e.getMessage());
        }
    }
}
