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
import system.service.AccountService;
import system.service.TransactionService;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/main/transfer")
public class TransferController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    private static final Logger log = LoggerFactory.getLogger(system.ApplicationWar.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String toTransfer(HttpSession session, Model model)
    {
        User user = UserUtils.getUserFromSession(session);
        model.addAttribute("user", user);
        return "transfer";
    }

    @RequestMapping(value= "/toMyAccount", method=RequestMethod.GET)
    public String openTransferFormTest(HttpSession session, Model model){
        User user = UserUtils.getUserFromSession(session);
        String senderAccountNumber = "";
        String receiverAccountNumber = "";
        model.addAttribute("user", user);
        model.addAttribute("senderAccountNumber", senderAccountNumber);
        model.addAttribute("receiverAccountNumber", receiverAccountNumber);

        if(senderAccountNumber != null && !senderAccountNumber.equals("") && receiverAccountNumber != null && !receiverAccountNumber.equals("")){
            model.addAttribute("accountsExist", true);
        }else{
            model.addAttribute("accountsExist", false);
        }
        model.addAttribute("amount", "0");
        model.addAttribute("user", user);
        model.addAttribute("all", user.getAccountList());
        return "toMyAccount";
    }

    @RequestMapping(value="/toMyAccount", method=RequestMethod.POST)
    public ModelAndView sendTransferForm(@ModelAttribute("amount") String amount, @ModelAttribute("senderAccountNumber") String senderAccountNumber,
                                         @ModelAttribute("receiverAccountNumber") String receiverAccountNumber, HttpSession session)
    {
        log.info("Параметры amount: " + amount + " senderAccountNumber: " + senderAccountNumber + " receiverAccountNumber: " + receiverAccountNumber);
        User user  = UserUtils.getUserFromSession(session);
        Account senderAccount = null;
        for (Account acc : user.getAccountList())
        {
            if(acc.getAccountNumber() == Long.parseLong(senderAccountNumber))
            {
                senderAccount = acc;
                break;
            }
        }

        Account receiverAccount = null;
        for (Account acc : user.getAccountList())
        {
            if(acc.getAccountNumber() == Long.parseLong(receiverAccountNumber))
            {
                receiverAccount = acc;
                break;
            }
        }

        log.info("Перервод: " + amount + " RUB со счета: " + senderAccountNumber + " на счет: " + receiverAccountNumber);
        Transaction transaction = transactionService.transfer(senderAccount, receiverAccount, Double.parseDouble(amount));
        if(transaction != null) return new ModelAndView("redirect:/main/accounts");
        return new ModelAndView("redirect:/main/transfer/toMyAccount");
    }

    @RequestMapping(value= "/toClient", method=RequestMethod.GET)
    public String openTransferFormTest1(HttpSession session, Model model){
        User user = UserUtils.getUserFromSession(session);
        String senderAccountNumber = "";
        String receiverAccountNumber = "";
        model.addAttribute("user", user);
        model.addAttribute("senderAccountNumber", senderAccountNumber);
        model.addAttribute("receiverAccountNumber", receiverAccountNumber);

        if(senderAccountNumber != null && !senderAccountNumber.equals("") && receiverAccountNumber != null && !receiverAccountNumber.equals("")){
            model.addAttribute("accountsExist", true);
        }else{
            model.addAttribute("accountsExist", false);
        }
        model.addAttribute("amount", "0");
        model.addAttribute("user", user);
        model.addAttribute("all", user.getAccountList());
        return "toClient";
    }

    @RequestMapping(value="/toClient", method=RequestMethod.POST)
    public ModelAndView sendTransferForm1(@ModelAttribute("amount") String amount, @ModelAttribute("senderAccountNumber") String senderAccountNumber,
                                         @ModelAttribute("receiverAccountNumber") String receiverAccountNumber, HttpSession session)
    {
        log.info("Параметры amount: " + amount + " senderAccountNumber: " + senderAccountNumber + " receiverAccountNumber: " + receiverAccountNumber);
        User user  = UserUtils.getUserFromSession(session);
        Account senderAccount = null;
        for (Account acc : user.getAccountList())
        {
            if(acc.getAccountNumber() == Long.parseLong(senderAccountNumber))
            {
                senderAccount = acc;
                break;
            }
        }

        Account receiverAccount = accountService.findByAccountNumber(Long.parseLong(receiverAccountNumber));

        log.info("Перервод: " + amount + " RUB со счета: " + senderAccountNumber + " на счет: " + receiverAccountNumber);
        Transaction transaction = transactionService.transfer(senderAccount, receiverAccount, Double.parseDouble(amount));
        if(transaction != null) return new ModelAndView("redirect:/main/accounts");
        return new ModelAndView("redirect:/main/transfer/toClient");
    }

    @RequestMapping(value = "/toAnotherBank", method = RequestMethod.GET)
    public String toOtherBank(HttpSession session, Model model)
    {
        User user = UserUtils.getUserFromSession(session);
        model.addAttribute("user", user);
        return "toAnotherBank";
    }
}
