package system.utils;


import system.entity.User;

import javax.servlet.http.HttpSession;

public class UserUtils {

    public static void saveUserToSession(HttpSession session, User user) {
        session.setAttribute("user", user);
    }

    public static User getUserFromSession(HttpSession session) {
        Object attribute = session.getAttribute("user");
        return attribute == null ? null : (User) attribute;
    }

    public static void deleteUserFromSession(HttpSession session)
    {
        session.removeAttribute("user");
    }


}
