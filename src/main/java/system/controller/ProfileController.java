package system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import system.entity.Account;
import system.entity.User;
import system.service.AccountService;
import system.service.UserService;
import system.utils.UserUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/main")
public class ProfileController {
    private static final Logger log = LoggerFactory.getLogger(system.Application.class);
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @RequestMapping( method = RequestMethod.GET)
    public String profile(HttpSession session, Model model)
    {
        User user = UserUtils.getUserFromSession(session);
        model.addAttribute("user", user);
        return "main";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session, Model model)
    {
        UserUtils.deleteUserFromSession(session);
        return "redirect:/";
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public String toAccounts(HttpSession session, Model model)
    {
        User user = UserUtils.getUserFromSession(session);
        model.addAttribute("list", user.getAccountList());
        log.info("---------------------------------------");
        log.info("Переходим на страницу Acoounts для пользователя: " + user);
        log.info("Аккаунты: " + user.getAccountList());
        log.info("---------------------------------------");
        return "accounts";
    }

}
