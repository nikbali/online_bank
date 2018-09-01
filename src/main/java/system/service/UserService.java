package system.service;


import system.entity.Action;
import system.entity.User;
import system.entity.UserRole;
import system.exceptions.IncorrectFieldsException;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> loadAll();

    void save(User user);

    void delete(User user);

    User findByLogin(String login);

    User findByEmail(String email);

    boolean checkUserExists(String login, String email);//при проверках на булеан лучше называть isUserExists

    boolean checkLoginExists(String login);

    boolean checkEmailExists(String email);

    boolean checkDocumentNumber(int document_number);

    User createUser(User user, Set<UserRole> roles) throws Exception;

    boolean checkFieldsBeforeCreate(String email,
                                    String login,
                                    String documentNumber,
                                    String first_name,
                                    String last_name,
                                    String middle_name,
                                    String password,
                                    String phone) throws IncorrectFieldsException;

    List<Action> findLastActions(User user);

}
