package system.controller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;
import system.service.AccountExporterService;
import system.service.AccountService;
import system.service.UserService;
import system.service.implemention.AccountExporterImpl;
import system.utils.UserUtils;
import system.utils.jaxb.JaxbUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
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

    @RequestMapping(value = "/export", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportAccountHistory(@ModelAttribute("accountNumber") String accountNumber, HttpSession session, HttpServletResponse response) {
        Account account = accountService.findByAccountNumber(Long.parseLong(accountNumber));
        User user = account.getUser();
        LOGGER.info("Start exporting data for user {} {}, account number {}.", user.getFirst_name(), user.getLast_name(), account.getAccountNumber());
        AccountExporterService accountExporterService = new AccountExporterImpl();
        List<Transaction> transactionList = accountExporterService.exportAccountHistory(account);
        String xml = JaxbUtils.marshalToXml(user, transactionList);
        InputStream inputStream = IOUtils.toInputStream(xml);
        try {
            IOUtils.copy(inputStream, response.getOutputStream());
            ServletOutputStream out = response.getOutputStream();
            out.flush();
            out.close();
        } catch (IOException e) {
           LOGGER.error("Error during convert and transfer data to xml.",e);
        }
    }
}
