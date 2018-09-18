package system.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import system.entity.User;
import system.entity.UserRole;
import system.utils.UserUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;


@WebFilter(urlPatterns = "/main/*")
public class UserFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(system.ApplicationWar.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        Enumeration<String> params = servletRequest.getParameterNames();
        HttpSession session = request.getSession();
        User user = UserUtils.getUserFromSession(session);

        if(user != null)
        {
            Set<UserRole> roles = user.getUserRoles();
            for (UserRole ur : roles)
            {
                if(ur.getRole().getName().equals("CLIENT"))filterChain.doFilter(servletRequest, servletResponse);
            }

        }

        log.info("WebFilter is invoked .");
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.sendRedirect("/index");

    }

    @Override
    public void destroy() {

    }
}
