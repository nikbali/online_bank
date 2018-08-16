package system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import system.entity.Account;
import system.entity.User;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/deposit")
public class DepositController {

    @RequestMapping(value="", method=RequestMethod.GET)
    public ModelAndView registration(@RequestParam(value="account", required = false) String account_number , HttpSession session)
    {
        ModelAndView model = new ModelAndView("deposit");
//        if(account_number != null)
//        {
//            model.addObject("account", account_number);
//        }
        Account account = new Account();
        model.addObject("account", account);
        User user = UserUtils.getUserFromSession(session);
        model.addObject("all", user.getAccountList());
        return model;
    }
}
