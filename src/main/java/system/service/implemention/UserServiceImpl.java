package system.service.implemention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import system.entity.User;
import system.repository.UserRepository;
import system.service.UserService;
import system.utils.CryptoUtils;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;



@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);


    public User createUser(User user) throws Exception
    {
        if(this.checkLoginExists(user.getLogin()))
        {
            LOG.info("Error create! User with Login:"+user.getLogin()+" already exist.");
            return userRepository.findByLogin(user.getLogin());
        }
        else
        {
            String crypt_pass = CryptoUtils.getHash(user.getPassword());
            user.setPassword(crypt_pass);
            //создаем ему аккаунт и добаляем
            return userRepository.save(user);
        }
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> loadAll() {
        return userRepository.findAll();
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean checkUserExist(String login, String email) {

        if((userRepository.findByEmail(email) != null)|| (userRepository.findByLogin(login) != null))
        {
            return true;
        }
        return false;
    }

    public boolean checkLoginExists(String login) {
        if(userRepository.findByLogin(login) != null) return true;
        return false;
    }

    public boolean checkEmailExists(String email) {
        if(userRepository.findByEmail(email) != null) return true;
        return false;
    }


}