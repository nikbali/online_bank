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
import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping("/main/deposit")
public class DepositController {
    @Autowired
    private TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(system.ApplicationWar.class);

    @RequestMapping(value="", method=RequestMethod.GET)
    public ModelAndView openDepositFormTest(@ModelAttribute("accountNumber") String accountNumber, HttpSession session)
    {
        User user = UserUtils.getUserFromSession(session);
        ModelAndView model = new ModelAndView("deposit");

        //если нам передали номер аккаунта для пополнения ставим флаг
        if(accountNumber != null && !accountNumber.equals("")){
            model.addObject("accountExist", true);
            model.addObject("accountNumber", Long.parseLong(accountNumber));
        }
        else
        {
            model.addObject("accountExist", false);
            model.addObject("accountNumber", 0L);
        }
        model.addObject("amount", "");
        model.addObject("user", user);
        model.addObject("all", user.getAccountList());
        return model;
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public ModelAndView sendDepositForm(@ModelAttribute("amount") String amount, @ModelAttribute("accountNumber") String accountNumber, HttpSession session)
    {
        log.info("Параметры amount: "+amount+" accountNumber: " + accountNumber );
        User user  = UserUtils.getUserFromSession(session);
        ModelAndView model = new ModelAndView("deposit");
        model.addObject("user", user);
        model.addObject("accountExist", true);
        model.addObject("accountNumber", Long.parseLong(accountNumber));

        String error_message = "";

        try{
            if(BigDecimal.valueOf(Double.parseDouble(amount)).compareTo(BigDecimal.ZERO) <= 0)
            {
                error_message = "Сумма пополнения должна быть больше нуля";
                log.info("Ошибка при пополнении  "+amount+" на  accountNumber: " + accountNumber + " :" + error_message);
                return model.addObject("errorTrue", true).addObject("textError", error_message);
            }
        }
        catch (NumberFormatException ex)
        {
            error_message = "Введенно некорректное значение в поле Amount";
            log.info("Ошибка при пополнении  "+amount+" на  accountNumber: " + accountNumber + " :" + error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }

        Account account = null;
        for (Account acc : user.getAccountList())
        {
            if(acc.getAccountNumber() == Long.parseLong(accountNumber))
            {
                account = acc;
                break;
            }
        }
        if(account == null) {
            error_message = "У вас нет Счета с таким номером, попробуйте снова";
            log.info("Ошибка при пополнении  "+amount+" на  accountNumber: " + accountNumber + " :" + error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        Transaction transaction = transactionService.deposit(account, BigDecimal.valueOf(Double.parseDouble(amount)));
        if(transaction == null) {
            error_message = "Ошибка на сервере, попробуйте позже";
            log.info("Ошибка при пополнении  "+amount+" на  accountNumber: " + accountNumber + " :" + error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }

        log.info("Пополнение  "+amount+" на  accountNumber: " + accountNumber );
        return model.addObject("errorFalse", true);

    }
}
