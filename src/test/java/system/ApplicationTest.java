package system;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import system.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest extends TestCase {

    private static final Logger log = LoggerFactory.getLogger(system.Application.class);

    @Autowired
    private UserService userService;

    @Test
    public void testMain() throws Exception {
        Assert.assertEquals(userService.checkEmailExists("nikbali@mail.ru"), true);
    }
}