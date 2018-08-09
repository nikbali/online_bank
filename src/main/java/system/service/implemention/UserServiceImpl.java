package system.service.implemention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.entity.User;
import system.entity.UserRole;
import system.exceptions.UserExistException;
import system.repository.RoleRepository;
import system.repository.UserRepository;
import system.service.AccountService;
import system.service.UserService;
import system.utils.CryptoUtils;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountService accountService;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);


    public User createUser(User user, Set<UserRole> roles) throws  Exception
    {
        if(this.checkLoginExists(user.getLogin()))
        {
            LOG.info("Error create! User with Login: %s already exists!", user.getLogin());
            throw new UserExistException("User with this Login already exists!", userRepository.findByLogin(user.getLogin()));
        }
        else if(this.checkDocumentNumber(user.getDocumentNumber()))
        {
            LOG.info("Error create! User with this Document Number: %d already exists!", user.getDocumentNumber());
            throw new UserExistException("User with this Document Number already exists!", userRepository.findByDocumentNumber(user.getDocumentNumber()));
        }
        else if (this.checkEmailExists(user.getEmail()))
        {
            LOG.info("Error create! User with Email: %s already exists!", user.getEmail());
            throw new UserExistException("User with this Email already exists!", userRepository.findByEmail(user.getEmail()));

        }
        else
        {
            //шифруем пароль
            String crypt_pass = CryptoUtils.getHash(user.getPassword());
            user.setPassword(crypt_pass);
            //добавляем роли
            for (UserRole role : roles) {
                roleRepository.save(role.getRole());
            }
            user.setUserRoles(roles);
            //создаем клиента и счет для клиента
            userRepository.save(user);
            Account account = accountService.createAccount(user);
            user.getAccountList().add(account);
            return user;
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

    public boolean checkUserExists(String login, String email) {

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

    public boolean checkDocumentNumber(int document_number) {
        if(userRepository.findByDocumentNumber(document_number) != null) return true;
        return false;
    }

    public boolean checkEmailExists(String email) {
        if(userRepository.findByEmail(email) != null) return true;
        return false;
    }


}