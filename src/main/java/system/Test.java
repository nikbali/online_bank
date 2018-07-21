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
import system.service.implemention.UserServiceImpl;

@SpringBootApplication
public class Test {

    private static final Logger log = LoggerFactory.getLogger(system.Application.class);

    public static void main(String[] args) {
        SpringApplication.run(system.Application.class);
    }

    /**
     *
     * Этот метод используется для тестинга, выводит в консоль в виде лога результаты всяких операций с БД

    @Bean
    public CommandLineRunner demo(UserRepository repository) {
        return (args) -> {
            // добавим в бд юзеров
            repository.save(new User("Jack", "123"));
            repository.save(new User("петя", "петя123"));
            repository.save(new User("вася", "вася123"));

            // fetch all Users
            log.info("User found with findAll():");
            log.info("-------------------------------");
            for (User user : repository.findAll()) {
                log.info(user.toString());
            }
            log.info("-------------------------------");
        };
    }
*/
}