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
import java.math.BigDecimal;

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
    public String openTransferFormTest(HttpSession session, Model model,
                                       @RequestParam(value = "amountErrorMsg", required = false) String amountErrorMsg,
                                       @RequestParam(value = "moneyErrorMsg", required = false) String moneyErrorMsg,
                                       @RequestParam(value = "accountsNumberErrorMsg", required = false) String accountsNumberErrorMsg){
        User user = UserUtils.getUserFromSession(session);
        String senderAccountNumber = "";
        String receiverAccountNumber = "";
        if (amountErrorMsg != null) {
            model.addAttribute("amountErrorMsg", amountErrorMsg);
        }
        if (moneyErrorMsg != null) {
            model.addAttribute("moneyErrorMsg", moneyErrorMsg);
        }
        if (accountsNumberErrorMsg != null) {
            model.addAttribute("accountsNumberErrorMsg", accountsNumberErrorMsg);
        }
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
    public ModelAndView sendTransferForm(@ModelAttribute("amount") String amount,
                                         @ModelAttribute("senderAccountNumber") String senderAccountNumber,
                                         @ModelAttribute("receiverAccountNumber") String receiverAccountNumber,
                                         HttpSession session){
        ModelAndView model = new ModelAndView("redirect:/main/transfer/toMyAccount");
        log.info("Параметры amount: " + amount + " senderAccountNumber: " + senderAccountNumber + " receiverAccountNumber: " + receiverAccountNumber);
        User user  = UserUtils.getUserFromSession(session);
        model.addObject("user", user);
        Account senderAccount = null;
        for (Account acc : user.getAccountList()){
            if(acc.getAccountNumber() == Long.parseLong(senderAccountNumber)){
                senderAccount = acc;
                break;
            }
        }

        Account receiverAccount = null;
        for (Account acc : user.getAccountList()){
            if(acc.getAccountNumber() == Long.parseLong(receiverAccountNumber)){
                receiverAccount = acc;
                break;
            }
        }

        log.info("Перервод: " + amount + " RUB со счета: " + senderAccountNumber + " на счет: " + receiverAccountNumber);
        Transaction transaction = transactionService.transfer(senderAccount, receiverAccount, BigDecimal.valueOf(Double.parseDouble(amount)));
        if(transaction != null) {
            return new ModelAndView("redirect:/main/accounts");
        }else{
            if(Double.parseDouble(amount) <= 0){
                model.addObject("amountErrorMsg", "Сумма должна быть больше нуля");
                log.error("Ошибка при перерводе между своими счетами, нулевая или отрицательная сумма {}", amount);
            }
            if(senderAccount.getAccount_balance().compareTo(BigDecimal.valueOf(Double.parseDouble(amount))) < 0){
                model.addObject("moneyErrorMsg", "У Вас недостаточно средств");
                log.error("Ошибка при перерводе между своими счетами, недостаточно средств, баланс {}, сумма {}", senderAccount.getAccount_balance(), amount);
            }
            if(senderAccount == receiverAccount){
                model.addObject("accountsNumberErrorMsg", "Аккаунты не должны совпадать");
                log.error("Ошибка при перерводе между своими счетами, senderAccount {} == receiverAccount {}", senderAccount.getAccountNumber(), receiverAccount.getAccountNumber());
            }
            return model;
        }
    }

    @RequestMapping(value= "/toClient", method=RequestMethod.GET)
    public String openTransferFormTest1(HttpSession session, Model model,
                @RequestParam(value = "amountErrorMsg", required = false) String amountErrorMsg,
                @RequestParam(value = "moneyErrorMsg", required = false) String moneyErrorMsg,
                @RequestParam(value = "accountsNumberErrorMsg", required = false) String accountsNumberErrorMsg,
                @RequestParam(value = "receiverErrorMsg", required = false) String receiverErrorMsg){
        User user = UserUtils.getUserFromSession(session);
        String senderAccountNumber = "";
        String receiverAccountNumber = "";
        if (amountErrorMsg != null) {
            model.addAttribute("amountErrorMsg", amountErrorMsg);
        }
        if (moneyErrorMsg != null) {
            model.addAttribute("moneyErrorMsg", moneyErrorMsg);
        }
        if (accountsNumberErrorMsg != null) {
            model.addAttribute("accountsNumberErrorMsg", accountsNumberErrorMsg);
        }
        if (receiverErrorMsg != null) {
            model.addAttribute("receiverErrorMsg", receiverErrorMsg);
        }
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
    public ModelAndView sendTransferForm1(@ModelAttribute("amount") String amount,
                                          @ModelAttribute("senderAccountNumber") String senderAccountNumber,
                                          @ModelAttribute("receiverAccountNumber") String receiverAccountNumber,
                                          HttpSession session){
        ModelAndView model = new ModelAndView("redirect:/main/transfer/toClient");
        log.info("Параметры amount: " + amount + " senderAccountNumber: " + senderAccountNumber + " receiverAccountNumber: " + receiverAccountNumber);
        User user  = UserUtils.getUserFromSession(session);
        Account senderAccount = null;
        for (Account acc : user.getAccountList()){
            if(acc.getAccountNumber() == Long.parseLong(senderAccountNumber)){
                senderAccount = acc;
                break;
            }
        }
        Account receiverAccount = null;
        receiverAccount = accountService.findByAccountNumber(Long.parseLong(receiverAccountNumber));
        if(receiverAccount == null){
            model.addObject("receiverErrorMsg", "Такого счета не существует");
            log.error("Ошибка при перерводе между клиентами, получателя не существует; receiverAccountNumber == {}", receiverAccountNumber);
            return model;
        }
        log.info("Перервод: " + amount + " RUB со счета: " + senderAccountNumber + " на счет: " + receiverAccountNumber);
        Transaction transaction = transactionService.transfer(senderAccount, receiverAccount, BigDecimal.valueOf(Double.parseDouble(amount)));
        if(transaction != null) {
            return new ModelAndView("redirect:/main/accounts");
        }else{
            if(Double.parseDouble(amount) <= 0){
                model.addObject("amountErrorMsg", "Сумма должна быть больше нуля");
                log.error("Ошибка при перерводе между клиентами, нулевая или отрицательная сумма {}", amount);
            }
            if(senderAccount.getAccount_balance().compareTo(BigDecimal.valueOf(Double.parseDouble(amount))) < 0){
                model.addObject("moneyErrorMsg", "У Вас недостаточно средств");
                log.error("Ошибка при перерводе между клиентами, недостаточно средств, баланс {}, сумма {}", senderAccount.getAccount_balance(), amount);
            }
            if(senderAccount.getAccountNumber() == receiverAccount.getAccountNumber()){
                model.addObject("accountsNumberErrorMsg", "Аккаунты не должны совпадать");
                log.error("Ошибка при перерводе между клиентами, senderAccount {} == receiverAccount {}", senderAccount.getAccountNumber(), receiverAccount.getAccountNumber());
            }
            log.error("номер получателя: {}", receiverAccount.getAccountNumber());
            return model;
        }
    }

    @RequestMapping(value = "/toAnotherBank", method = RequestMethod.GET)
    public String toOtherBank(HttpSession session, Model model)
    {
        User user = UserUtils.getUserFromSession(session);
        model.addAttribute("user", user);
        return "toAnotherBank";
    }
}
