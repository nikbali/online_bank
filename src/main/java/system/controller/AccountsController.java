package system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;
import system.exceptions.AccountNotFoundException;
import system.repository.TransactionRepository;
import system.service.AccountExporterService;
import system.service.AccountService;
import system.service.UserService;
import system.service.implemention.AccountExporterImpl;
import system.utils.UserUtils;
import system.utils.jaxb.JaxbUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/main/accounts")
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
            res.setStatus(200);
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
        List<Transaction> transactionList = getTransactionsList(accountNumber);
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

    @RequestMapping(value = "/exportCSV", method = RequestMethod.GET)
    public @ResponseBody String exportAccountHistoryCsv(@ModelAttribute("accountNumber") String accountNumber) {
        List<Transaction> transactionList = getTransactionsList(accountNumber);
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


    /**
     * Возвращает список операций по счету
     * @param accountNumber номер счета
     * @return лист операций
     */
    private List<Transaction> getTransactionsList(String accountNumber) {
        Account account = accountService.findByAccountNumber(Long.parseLong(accountNumber));
        User user = account.getUser();
        LOGGER.info("Start exporting data for user {} {}, account number {}.", user.getFirst_name(), user.getLast_name(), account.getAccountNumber());
        AccountExporterService accountExporterService = new AccountExporterImpl();
        return accountExporterService.exportAccountHistory(account);
    }



    @Autowired
    private TransactionRepository transactionRepository;
    /**
     * Возвращает список операций по счету для страничного вывода по 10 записей
     * @param accountNumber номер счета,
     * @return лист операций
     */
    private List<Transaction> getTransactionsListForPaggination(String accountNumber, int pageNumber) {
        Account account = accountService.findByAccountNumber(Long.parseLong(accountNumber));
        return transactionRepository.findOperationsForPaddination(account.getId(), pageNumber*10, 10);
    }
    private long  getTransactionsCount(String accountNumber) {
        Account account = accountService.findByAccountNumber(Long.parseLong(accountNumber));
        return transactionRepository.countOperations(account.getId());
    }


    @RequestMapping(value = {"/history"}, method = RequestMethod.GET)
    public ModelAndView getHistoryOperationView(@RequestParam(value = "account_number", required = false) String account_number,
                                                @PageableDefault(size = 10) Pageable pageable,
                                                HttpSession session )
    {
        ModelAndView model = new ModelAndView("history_operations");
        if (StringUtils.isEmpty(account_number)) {
            throw new RuntimeException("Обязательный параметр account_number не заполнен!");
        }
        User user = UserUtils.getUserFromSession(session);
        Account account = accountService.findByAccountNumber(Long.parseLong(account_number));
        if(account == null)
        {
            throw new AccountNotFoundException("Такой account_number не найден!");
        }

        Page<Transaction> page = transactionRepository.findAllOperationForAccount(account.getId(), pageable);

        for(Account acc : user.getAccountList()){
            if (acc.getAccountNumber() == account.getAccountNumber()) {
                model.addObject("user", user);
                model.addObject("account", account);
                model.addObject("page", page);
                LOGGER.info("Переход на стрницу %s с историей операций для аккаунта = %s.", page.getNumber(), account_number);
                return model;
            }
        }
        LOGGER.error("Страница истории операций для заданного аккаунта недоступна.");
        return new ModelAndView("redirect:/");
    }


}
