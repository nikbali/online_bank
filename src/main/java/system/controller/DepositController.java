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

@Controller
@RequestMapping("/deposit")
public class DepositController {
    @Autowired
    private TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(system.Application.class);

    @RequestMapping(value="", method=RequestMethod.GET)
    public ModelAndView openDepositForm(@RequestParam(value="account", required = false) String account_number , HttpSession session)
    {
        ModelAndView model = new ModelAndView("deposit");
//        if(account_number != null)
//        {
//            model.addObject("account", account_number);
//        }
        Transaction deposit = new Transaction();
        model.addObject("operation", deposit);
        User user = UserUtils.getUserFromSession(session);
        model.addObject("all", user.getAccountList());
        return model;
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public ModelAndView sendDepositForm(@ModelAttribute("operation") Transaction operation)
    {
        log.info("Есть инфа об транзакции: " + operation.getAmount() + " Отправитель: " + operation.getSender().getAccountNumber());
        Transaction transaction = transactionService.deposit(operation.getSender(), operation.getAmount());
        if(transaction != null) return new ModelAndView("accounts");
        return new ModelAndView("user_list");
    }
}
