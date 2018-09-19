package system.filters;

import org.slf4j.LoggerFactory;
import system.entity.User;
import system.entity.UserRole;
import system.utils.UserUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;


@WebFilter(urlPatterns = "/admin/*")
public class AdminFilter implements Filter {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(system.ApplicationWar.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpSession session = request.getSession();
        User user = UserUtils.getUserFromSession(session);
        boolean accses = false;
        if(user != null ) {
            Set<UserRole> roles = user.getUserRoles();
            for (UserRole ur : roles) {
                if(ur.getRole().getName().equals("ADMIN"))accses = true;
            }
        }
        if(accses){
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            log.info("Admin WebFilter is invoked .");
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.sendRedirect("/index");
        }
    }

    @Override
    public void destroy() {

    }
}
