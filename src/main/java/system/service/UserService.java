package system.service;

import system.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> loadAll();

    void save(User user);

    void delete(User user);

    User findByLogin(String login);

    User findByEmail(String email);

    boolean checkUserExist(String login, String email);

    boolean checkLoginExists(String login);

    boolean checkEmailExists(String email);

    User createUser(User user);
}
