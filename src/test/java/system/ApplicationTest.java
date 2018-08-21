package system;

import junit.framework.TestCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.entity.User;
import system.service.AccountService;
import system.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest extends TestCase {

    private static final Logger log = LoggerFactory.getLogger(system.ApplicationWar.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;


    @Test
    public void testMain() throws Exception {
        String email = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
        String login = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
        long acc_num = RandomUtils.nextLong(1000, 10000);

        Assert.assertEquals(someTransactionalMethod(email, login, acc_num), login);
    }

    @Transactional(rollbackFor={Exception.class})
    public String someTransactionalMethod(String email, String login, long acc){
        User user = new User(email,RandomUtils.nextInt(10000000,20000000), login, "first_name", "last_name", "middle_name", "password", "phone");
        Account account = new Account(0.0, acc, user);

        userService.save(user);
        accountService.save(account);
        return accountService.findByAccountNumber(acc).getUser().getLogin();
    }

}