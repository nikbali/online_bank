package system.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import system.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;


@WebFilter(urlPatterns = "/profile")
public class MyFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(system.Application.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("MyFilter doFilter() is invoked.");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        Enumeration<String> params = servletRequest.getParameterNames();
        HttpSession session = request.getSession();
        User attr = (User)session.getAttribute("user");

        if(attr != null)filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
