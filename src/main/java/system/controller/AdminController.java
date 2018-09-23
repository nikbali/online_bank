package system.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import system.entity.Account;
import system.entity.User;
import system.service.AccountService;
import system.service.UserService;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;

/**
 * Контроллер обрабатывает /admin/*  запросы, для пользователей с ролью ADMIN
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(system.ApplicationWar.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @RequestMapping( method = RequestMethod.GET)
    public ModelAndView toMainAdminView(HttpSession session)
    {
        ModelAndView model = new ModelAndView("admin");
        User user = UserUtils.getUserFromSession(session);
        model.addObject("user", user);
        model.addObject("clients", userService.loadAll());
        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session)
    {
        User user = UserUtils.getUserFromSession(session);
        UserUtils.deleteUserFromSession(session);
        log.info(String.format("Пользователь: %s! Вышел из системы.", user.getLogin()));
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public ModelAndView toDownloadReportView(@RequestParam(value = "login", required = false) String login)
    {
        if(StringUtils.isEmpty(login)){
            return new ModelAndView("reports");
        }
        return new ModelAndView("reports_for_user").addObject("login", login);
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ModelAndView toAccountInfoView(@RequestParam(value = "account", required = false) String account_number)
    {
        if(StringUtils.isEmpty(account_number)){
            return new ModelAndView("redirect:/admin");
        }
        Account account = accountService.findByAccountNumber(Long.parseLong(account_number));
        if(account == null){
            return new ModelAndView("redirect:/admin");
        }
        return new ModelAndView("accountForAdmin").addObject("account", account);
    }


}
