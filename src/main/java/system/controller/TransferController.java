package system.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
                                       @RequestParam(value = "errorTrue", required = false) Boolean errorTrue,
                                       @RequestParam(value = "errorFalse", required = false) Boolean errorFalse,
                                       @RequestParam(value = "textError", required = false) String textError){
        User user = UserUtils.getUserFromSession(session);
        String senderAccountNumber = "";
        String receiverAccountNumber = "";

        if (errorTrue != null) {
            model.addAttribute("errorTrue", errorTrue);
        }
        if (errorFalse != null) {
            model.addAttribute("errorFalse", errorFalse);
        }
        if (textError != null) {
            model.addAttribute("textError", textError);
        }
        model.addAttribute("user", user);
        model.addAttribute("senderAccountNumber", senderAccountNumber);
        model.addAttribute("receiverAccountNumber", receiverAccountNumber);

        if(senderAccountNumber != null && !senderAccountNumber.equals("") && receiverAccountNumber != null && !receiverAccountNumber.equals("")){
            model.addAttribute("accountsExist", true);
        }else{
            model.addAttribute("accountsExist", false);
        }
        model.addAttribute("amount", "");
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
        User user  = UserUtils.getUserFromSession(session);
        model.addObject("user", user);
        String error_message = "";

        try{
            if(BigDecimal.valueOf(Double.parseDouble(amount)).compareTo(BigDecimal.ZERO) <= 0)
            {
                error_message = "Сумма перевода должна быть больше нуля";
                log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
                return model.addObject("errorTrue", true).addObject("textError", error_message);
            }
        }
        catch (NumberFormatException ex)
        {
            error_message = "Введенно некорректное значение в поле Amount";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }


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

        if(senderAccount == null) {
            error_message = "У вас нет cчета с номером " + senderAccountNumber + ", попробуйте снова";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        if(receiverAccount == null) {
            error_message = "У вас нет cчета с номером " + receiverAccountNumber + ", попробуйте снова";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        if(Double.parseDouble(amount) <= 0){
            error_message = "Amount должен быть больше нуля";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        if(senderAccount.getAccount_balance().compareTo(BigDecimal.valueOf(Double.parseDouble(amount))) < 0){
            error_message = "На счете " + senderAccountNumber + "  недостаточно средств";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        if(senderAccount == receiverAccount){
            error_message = "Счета не должны совпадать";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }

        Transaction transaction = transactionService.transfer(senderAccount, receiverAccount, BigDecimal.valueOf(Double.parseDouble(amount)));
        if(transaction == null) {
            error_message = "Ошибка на сервере, попробуйте позже";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        log.info("Перевод: {} RUB со счета: {} на счет: {}", amount, senderAccountNumber, receiverAccountNumber);
        return model.addObject("errorFalse", true);
    }

    @RequestMapping(value= "/toClient", method=RequestMethod.GET)
    public String openTransferFormTest1(HttpSession session, Model model,
                                        @RequestParam(value = "errorTrue", required = false) Boolean errorTrue,
                                        @RequestParam(value = "errorFalse", required = false) Boolean errorFalse,
                                        @RequestParam(value = "textError", required = false) String textError){
        User user = UserUtils.getUserFromSession(session);
        String senderAccountNumber = "";
        String receiverAccountNumber = "";
        if (errorTrue != null) {
            model.addAttribute("errorTrue", errorTrue);
        }
        if (errorFalse != null) {
            model.addAttribute("errorFalse", errorFalse);
        }
        if (textError != null) {
            model.addAttribute("textError", textError);
        }
        model.addAttribute("user", user);
        model.addAttribute("senderAccountNumber", senderAccountNumber);
        model.addAttribute("receiverAccountNumber", receiverAccountNumber);

        if(senderAccountNumber != null && !senderAccountNumber.equals("") && receiverAccountNumber != null && !receiverAccountNumber.equals("")){
            model.addAttribute("accountsExist", true);
        }else{
            model.addAttribute("accountsExist", false);
        }
        model.addAttribute("amount", "");
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
        User user  = UserUtils.getUserFromSession(session);
        //model.addObject("user", user);
        String error_message = "";

        try{
            if(BigDecimal.valueOf(Double.parseDouble(amount)).compareTo(BigDecimal.ZERO) <= 0)
            {
                error_message = "Сумма перевода должна быть больше нуля";
                log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
                return model.addObject("errorTrue", true).addObject("textError", error_message);
            }
        }
        catch (NumberFormatException ex)
        {
            error_message = "Введенно некорректное значение в поле Amount";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }

        Account senderAccount = null;
        for (Account acc : user.getAccountList()){
            if(acc.getAccountNumber() == Long.parseLong(senderAccountNumber)){
                senderAccount = acc;
                break;
            }
        }

        Account receiverAccount = accountService.findByAccountNumber(Long.parseLong(receiverAccountNumber));

        if(senderAccount == null) {
            error_message = "У вас нет cчета с номером " + senderAccountNumber + ", попробуйте снова";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        if(receiverAccount == null) {
            error_message = "Cчета с номером " + receiverAccountNumber + " не существует, попробуйте снова";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        if(Double.parseDouble(amount) <= 0){
            error_message = "Amount должен быть больше нуля";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        if(senderAccount.getAccount_balance().compareTo(BigDecimal.valueOf(Double.parseDouble(amount))) < 0){
            error_message = "На счете " + senderAccountNumber + "  недостаточно средств";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        if(senderAccount == receiverAccount){
            error_message = "Счета не должны совпадать";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }

        Transaction transaction = transactionService.transfer(senderAccount, receiverAccount, BigDecimal.valueOf(Double.parseDouble(amount)));
        if(transaction == null) {
            error_message = "Ошибка на сервере, попробуйте позже";
            log.info("Ошибка при переводе {} c senderAccountNumber: {} на  receiverAccountNumber: {} : {}", amount, senderAccountNumber, receiverAccountNumber, error_message);
            return model.addObject("errorTrue", true).addObject("textError", error_message);
        }
        log.info("Перевод: {} RUB со счета: {} на счет: {}", amount, senderAccountNumber, receiverAccountNumber);
        return model.addObject("errorFalse", true);
    }


    @RequestMapping(value= "/toAnotherBank", method=RequestMethod.GET)
    public ModelAndView transferToAnotherBank(HttpSession session,
                                        @RequestParam(value = "errorTrue", required = false) Boolean errorTrue,
                                        @RequestParam(value = "errorFalse", required = false) Boolean errorFalse,
                                        @RequestParam(value = "textError", required = false) String textError){
        ModelAndView model = new ModelAndView("toAnotherBank");
        User user = UserUtils.getUserFromSession(session);
        String senderAccountNumber = "";
        String receiverAccountNumber = "";
        if (errorTrue != null) {
            model.addObject("errorTrue", errorTrue);
        }
        if (errorFalse != null) {
            model.addObject("errorFalse", errorFalse);
        }
        if (textError != null) {
            model.addObject("textError", textError);
        }
        model.addObject("user", user);
        model.addObject("senderAccountNumber", senderAccountNumber);
        model.addObject("receiverAccountNumber", receiverAccountNumber);

        if(senderAccountNumber != null && !senderAccountNumber.equals("") && receiverAccountNumber != null && !receiverAccountNumber.equals("")){
            model.addObject("accountsExist", true);
        }else{
            model.addObject("accountsExist", false);
        }
        model.addObject("amount", "");
        model.addObject("user", user);
        model.addObject("all", user.getAccountList());
        return model;
    }
}
