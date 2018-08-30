package system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;
import system.service.AccountExporterService;
import system.service.AccountService;
import system.service.UserService;
import system.service.implemention.AccountExporterImpl;
import system.utils.UserUtils;
import system.utils.jaxb.JaxbUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountsController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    private static final Logger LOGGER = LoggerFactory.getLogger(system.ApplicationWar.class);

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String createNewAccountForClient(HttpSession session, Model model, HttpServletResponse res) {
        User user = UserUtils.getUserFromSession(session);
        if (user.getAccountList().size() >= 5) {
            res.setStatus(501);
            res.addHeader("Error", "The customer can not create more than 5 accounts");
            return null;
        } else {
            Account account = accountService.createAccount(user);
            user.getAccountList().add(account);
            model.addAttribute("acc", account);
            res.setStatus(201);
            return "fragments :: account";
        }
    }

    @RequestMapping(value = "/exportXML", method = RequestMethod.GET, produces = {"application/xml"})
    public @ResponseBody String exportAccountHistoryXml(@ModelAttribute("accountNumber") String accountNumber) {
        List<Transaction> transactionList = getTransactionsList(accountNumber);
        String xml = JaxbUtils.marshalToXml(transactionList);
        LOGGER.info("XML created.");
        return xml;
    }


    @RequestMapping(value = "/exportJSON", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody String exportAccountHistoryJson(@ModelAttribute("accountNumber") String accountNumber) {
        List<Transaction> transactionList = getTransactionsList(accountNumber);
        String json = "";
        try {
            json = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(transactionList);
            LOGGER.info("Json string is created.");
        } catch (JsonProcessingException e) {
            LOGGER.error("Json string is created with error.",e);
        }
        return json;
    }

    //TODO сделать красивый вывод в csv
    @RequestMapping(value = "/exportCSV", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody String exportAccountHistoryCsv(@ModelAttribute("accountNumber") String accountNumber) {
        List<Transaction> transactionList = getTransactionsList(accountNumber);

        return null;
    }


    private List<Transaction> getTransactionsList(String accountNumber) {
        Account account = accountService.findByAccountNumber(Long.parseLong(accountNumber));
        User user = account.getUser();
        LOGGER.info("Start exporting data for user {} {}, account number {}.", user.getFirst_name(), user.getLast_name(), account.getAccountNumber());
        AccountExporterService accountExporterService = new AccountExporterImpl();
        return accountExporterService.exportAccountHistory(account);
    }
}
