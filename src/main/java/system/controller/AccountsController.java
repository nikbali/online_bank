package system.controller;

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
@RequestMapping("/accounts")
public class AccountsController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String createNewAccountForClient(HttpSession session, Model model, HttpServletResponse res) {
        User user = UserUtils.getUserFromSession(session);
        if(user.getAccountList().size() >= 5)
        {
            res.setStatus(501);
            res.addHeader("Error", "The customer can not create more than 5 accounts");
            return null;
        }
        else {
            Account account = accountService.createAccount(user);
            user.getAccountList().add(account);
            model.addAttribute("acc", account);
            res.setStatus(201);
            return "fragments :: account";
        }
    }

}
