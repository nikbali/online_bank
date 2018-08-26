package system.utils;

import system.entity.User;

import javax.servlet.http.HttpSession;

public class AccountUtils {
    public static String getAccountNumberFromSession(HttpSession session) {
        Object attribute = session.getAttribute("accountNumber");
        return attribute == null ? null : (String) attribute;
    }
}
