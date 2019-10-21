package ru.otus.hw12.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        var authorization = req.getHeader("Authorization");

        if (authorization != null && authorization.equals("123456")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            res.setStatus(403);
        }
    }

    @Override
    public void destroy() {
    }
}
