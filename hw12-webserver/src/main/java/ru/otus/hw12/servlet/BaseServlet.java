package ru.otus.hw12.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("ALL")
public class BaseServlet extends HttpServlet {
    protected static final String APPLICATION_JSON = "application/json;charset=UTF-8";
    protected final Gson gson;

    public BaseServlet() {
        this.gson = new Gson();
    }

    protected void json(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType(APPLICATION_JSON);
        try (var outputStream = resp.getOutputStream()) {
            outputStream.print(gson.toJson(data));
        }
    }
}
