package system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;
import system.service.TransactionService;
import system.service.UserService;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/deposit")
public class DepositController {
    @Autowired
    private TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(system.Application.class);

    @RequestMapping(value="", method=RequestMethod.GET)
    public ModelAndView openDepositForm(HttpSession session)
    {
        User user = UserUtils.getUserFromSession(session);
        ModelAndView model = new ModelAndView("deposit");
        model.addObject("accountNumber", 0L);
        model.addObject("amount", "");
        model.addObject("all", user.getAccountList());

        Transaction deposit = new Transaction();
        model.addObject("operation", deposit);
        return model;
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public ModelAndView sendDepositForm(@ModelAttribute("amount") String amount, @ModelAttribute("accountNumber") String accountNumber, HttpSession session)
    {
        log.info("Параметры amount: "+amount+" accountNumber: " + accountNumber );
        User user  = UserUtils.getUserFromSession(session);
        Account account = null;
        for (Account acc : user.getAccountList())
        {
            if(acc.getAccountNumber() == Long.parseLong(accountNumber))
            {
                account = acc;
                break;
            }
        }
        log.info("Депозит: " + amount + " RUB На Счет: " + accountNumber);
        Transaction transaction = transactionService.deposit(account, Double.parseDouble(amount));
        if(transaction != null) return new ModelAndView("redirect:/main/accounts");
        return new ModelAndView("user_list");
    }


}
