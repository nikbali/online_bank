package system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import system.entity.User;
import system.service.TransactionService;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/main/transfer")
public class TransferController {
    @Autowired
    private TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(system.ApplicationWar.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String toTransfer(HttpSession session, Model model)
    {
        User user = UserUtils.getUserFromSession(session);
        model.addAttribute("user", user);
        return "transfer";
    }

    @RequestMapping(value = "/toMyAccount", method = RequestMethod.GET)
    public String toMyAccount(HttpSession session, Model model)
    {
        User user = UserUtils.getUserFromSession(session);
        model.addAttribute("user", user);
        return "toMyAccount";
    }

    @RequestMapping(value = "/toClient", method = RequestMethod.GET)
    public String toClient(HttpSession session, Model model)
    {
        User user = UserUtils.getUserFromSession(session);
        model.addAttribute("user", user);
        return "toClient";
    }

    @RequestMapping(value = "/toAnotherBank", method = RequestMethod.GET)
    public String toOtherBank(HttpSession session, Model model)
    {
        User user = UserUtils.getUserFromSession(session);
        model.addAttribute("user", user);
        return "toAnotherBank";
    }
}
