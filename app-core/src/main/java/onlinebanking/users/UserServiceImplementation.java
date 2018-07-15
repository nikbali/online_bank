package onlinebanking.users;

import onlinebanking.OnlineBankingRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public List<User> loadAll() {
        return userRepository.loadAll();
    }

    @Override
    public User save(User user) {
        if(userRepository.loadAll().contains(user)){
            throw new OnlineBankingRuntimeException(String.format("User with name %s is already exists.", user.getName()));
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id);
    }
}
