package system.controller;

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
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
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
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
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

    @RequestMapping(value = "/exportXML", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody
    String exportAccountHistoryXml(@ModelAttribute("accountNumber") String accountNumber) {
        List<Transaction> transactionList = getTransactionsList(accountNumber);
        String xml = JaxbUtils.marshalToXml(transactionList);
        LOGGER.info("XML created.");
        return xml;
    }


    @RequestMapping(value = "/exportJSON", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    String exportAccountHistoryJson(@ModelAttribute("accountNumber") String accountNumber) {
        List<Transaction> transactionList = getTransactionListForJson(getTransactionsList(accountNumber));
        String json = "";
        try {
            json = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(transactionList);
            LOGGER.info("Json string is created.");
        } catch (Exception e) {
            LOGGER.error("Json string is created with error.", e);
        }
        return json;
    }

    //TODO сделать красивый вывод в csv
    @RequestMapping(value = "/exportCSV", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody String exportAccountHistoryCsv(@ModelAttribute("accountNumber") String accountNumber) {
        List<Transaction> transactionList = getTransactionListForJson(getTransactionsList(accountNumber));
        StringWriter stringWriter = new StringWriter();
        try (ICsvBeanWriter beanWriter = new CsvBeanWriter(stringWriter, CsvPreference.STANDARD_PREFERENCE)) {
            beanWriter.writeHeader(Transaction.NAMES);
            for (final Transaction transaction : transactionList) {
                beanWriter.write(transaction,Transaction.COLUMNS);
            }
        } catch (IOException e) {
            LOGGER.error("CSV string is created with error.", e);
        }
        LOGGER.info("CSV is created {}.",stringWriter.toString());
        return stringWriter.toString();
    }


    private List<Transaction> getTransactionsList(String accountNumber) {
        Account account = accountService.findByAccountNumber(Long.parseLong(accountNumber));
        User user = account.getUser();
        LOGGER.info("Start exporting data for user {} {}, account number {}.", user.getFirst_name(), user.getLast_name(), account.getAccountNumber());
        AccountExporterService accountExporterService = new AccountExporterImpl();
        return accountExporterService.exportAccountHistory(account);
    }

    //список транзакций для вывода клиенту
    public List<Transaction> getTransactionListForJson(List<Transaction> list) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : list) {
            transactions.add(new Transaction(transaction.getAmount(),
                    transaction.getDate(),
                    transaction.getDescription(),
                    transaction.getStatus(),
                    transaction.getType(),
                    new Account(transaction.getSender().getAccount_balance(),
                            transaction.getSender().getAccountNumber(),
                            new User(transaction.getSender().getUser().getFirst_name(),
                                    transaction.getSender().getUser().getLast_name(),
                                    transaction.getSender().getUser().getMiddle_name())),
                    new Account(transaction.getReciever().getAccount_balance(),
                            transaction.getReciever().getAccountNumber(),
                            new User(transaction.getReciever().getUser().getFirst_name(),
                                    transaction.getReciever().getUser().getLast_name(),
                                    transaction.getReciever().getUser().getMiddle_name()))));
        }
        return transactions;
    }
}
