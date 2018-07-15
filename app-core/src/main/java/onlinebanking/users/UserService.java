package onlinebanking.users;

import java.util.List;

public interface UserService {
    List<User> loadAll();

    //save user and return user with id
    User save(User user);

    void delete(int id);

    void deleteAll();

    User findByName(String name);

    User findById(int id);



}
