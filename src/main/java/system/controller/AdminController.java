package system.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;
import system.repository.ConvertStrategy;
import system.service.AccountService;
import system.service.TransactionService;
import system.service.UserService;
import system.utils.UserUtils;
import system.utils.jaxb.JaxbUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Контроллер обрабатывает /admin/*  запросы, для пользователей с ролью ADMIN
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(system.ApplicationWar.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;


    @RequestMapping( method = RequestMethod.GET)
    public ModelAndView toMainAdminView(HttpSession session)
    {
        ModelAndView model = new ModelAndView("admin");
        User user = UserUtils.getUserFromSession(session);
        model.addObject("user", user);
        model.addObject("clients", userService.loadAll());
        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session)
    {
        User user = UserUtils.getUserFromSession(session);
        UserUtils.deleteUserFromSession(session);
        log.info(String.format("Пользователь: %s! Вышел из системы.", user.getLogin()));
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public ModelAndView toDownloadReportView(@RequestParam(value = "login", required = false) String login)
    {
        if(StringUtils.isEmpty(login)){
            return new ModelAndView("reports");
        }
        return new ModelAndView("reports_for_user").addObject("login", login);
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ModelAndView toAccountInfoView(@RequestParam(value = "account", required = false) String account_number)
    {
        if(StringUtils.isEmpty(account_number)){
            return new ModelAndView("redirect:/admin");
        }
        Account account = accountService.findByAccountNumber(Long.parseLong(account_number));
        if(account == null){
            return new ModelAndView("redirect:/admin");
        }
        return new ModelAndView("accountForAdmin").addObject("account", account);
    }


    /**
     * Метод обрабатывает post - запросы по выгрузке отчетов в различных форматах
     * @param login клиента
     * @param response ServletResponse
     * @param fromDate дата в формате ДД.ММ.ГГГГ начало диапозона
     * @param toDate дата в формате ДД.ММ.ГГГГ конца диапозона
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView export(@ModelAttribute("login")    String login,
                @ModelAttribute("fromDate") String fromDate,
                @ModelAttribute("toDate")   String toDate,
                HttpServletResponse response) {
        User user = userService.findByLogin(login);
        Date from, to;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            from = fromDate.equals("")? new Date(0):format.parse(fromDate);
            to =   toDate.equals("")? new Date():format.parse(toDate);
        }catch (ParseException ex) {
            return new ModelAndView("redirect:/admin");
        }
        List<Transaction> transactionList;
        if (user == null) {
            transactionList = transactionService.loadAllTransactionsForRangeDate(from, to);
        } else {
            transactionList = transactionService.loadAllTransactionsForRangeDateByUser(user, from, to);
        }

        String xml = JaxbUtils.marshalToXml(transactionList);
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment;filename=export_"+LocalDate.now().toString() +".xml");
        try {
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(xml.getBytes());
            outStream.flush();
            outStream.close();
        }catch (IOException e){
           response.setStatus(501);
           return new ModelAndView("redirect:/admin");
        }
        return new ModelAndView("redirect:/admin/reports");
    }
}
