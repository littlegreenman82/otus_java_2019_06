package ru.otus.hw12.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var username = req.getParameter("username");
        var password = req.getParameter("password");

        if (username.equals("admin") && password.equals("admin")) {
            this.json(resp, new AuthResult(true, "123456"));
        } else {
            this.json(resp, new AuthResult(false, null));
        }
    }

    private static class AuthResult {
        private Boolean success;
        private String token;

        private AuthResult(Boolean success, String token) {
            this.success = success;
            this.token = token;
        }
    }
}
