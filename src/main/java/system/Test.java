package system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import system.entity.User;
import system.repository.UserRepository;
import system.service.UserService;


@SpringBootApplication
public class Test {

    private static final Logger log = LoggerFactory.getLogger(system.Application.class);

    public static void main(String[] args) {
        SpringApplication.run(system.Application.class);
    }

    /**
     *
     * Этот метод используется для тестинга, выводит в консоль в виде лога результаты всяких операций с БД
    */
    @Bean
    public CommandLineRunner demo(UserService service) {

        return  (args) -> {

            User user1 = new User("king@mail.ru" , "king777", "Петя", "Иванов", "dificult", "87779912");
            service.createUser(user1);
            log.info("User found with findAll():");
            log.info("-------------------------------");
            for (User user : service.loadAll()) {
                log.info(user.toString());
            }
            log.info("-------------------------------");
        };

    }

}