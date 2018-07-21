package system.service.implemention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import system.entity.User;
import system.repository.UserRepository;
import system.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;



@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    public List<User> loadAll() {
        return null;
    }

    public User save(User user) {
        return null;
    }

    public void delete(int id) {

    }

    public User findByName(String name) {
        return null;
    }

    public User findById(int id) {
        return null;
    }
}