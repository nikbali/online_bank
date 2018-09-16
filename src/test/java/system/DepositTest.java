package system;
import junit.framework.TestCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;
import system.enums.AccountType;
import system.enums.Currency;
import system.enums.StatusOperation;
import system.enums.TypeOperation;
import system.service.AccountService;
import system.service.TransactionService;
import system.service.UserService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DepositTest extends TestCase {

    private static final Logger log = LoggerFactory.getLogger(system.ApplicationWar.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    private Account account;
    private double amount = RandomUtils.nextDouble(0,100000.0);

    @Before
    public void initData(){
        User user = new User(RandomStringUtils.random(10,
                "qwertyuiopasdfghjkl"),
                RandomUtils.nextInt(10000000,20000000),
                RandomStringUtils.random(10,"qwertyuiopasdfghjkl"),
                "unit_test",
                "unit_test",
                "unit_test",
                "unit_test",
                "unit_test"
        );
        log.info("Create User: " + user);
        account = new Account(BigDecimal.ZERO, RandomUtils.nextLong(100000, 1000000), user, Currency.RUB, AccountType.DEBIT, 100);
        log.info("Create Account: " + account);
        userService.save(user);
        accountService.save(account);
    }

    //ПОЛОЖИТЕЛЬНЫЕ ТЕСТ-КЕЙСЫ
    @Test
    public void successDepositToAccount(){
        Transaction transaction = transactionService.deposit(account, BigDecimal.valueOf(amount));
            log.info("Make Deposit: " + transaction);
        Assert.assertEquals("Неправильное значение баланса на Аккаунте", BigDecimal.valueOf(amount), accountService.findById(account.getId()).get().getAccount_balance());
        Assert.assertEquals("Неправильное значение поля Status на Transaction",StatusOperation.DONE, transaction.getStatus());
        Assert.assertEquals("Неправильное значение поля Type на Transaction",TypeOperation.DEPOSIT, transaction.getType());
        Assert.assertEquals("Неправильный отправитель на Transaction",account.getAccountNumber(), transaction.getSender().getAccountNumber());
        Assert.assertEquals("Неправильный получатель на Transaction",account.getAccountNumber(), transaction.getReciever().getAccountNumber());


    }




}