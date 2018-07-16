package system.service;

import system.entity.User;

import java.util.List;

public interface UserService {
    List<User> loadAll();

    User save(User user);

    void delete(int id);

    User findByName(String name);

    User findById(int id);
}
