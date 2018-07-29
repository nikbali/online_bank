package system.service;

import system.entity.User;

import java.util.List;

public interface UserService {
    List<User> loadAll();

    void save(User user);

    void delete(User user);

    User findByLogin(String login);

    User findByEmail(String email);

    boolean checkUserExists(String login, String email);//при проверках на булеан лучше называть isUserExists

    boolean checkLoginExists(String login);

    boolean checkEmailExists(String email);

    User createUser(User user) throws Exception;
}
