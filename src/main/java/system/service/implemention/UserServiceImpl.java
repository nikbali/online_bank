package system.service.implemention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import system.entity.*;
import system.exceptions.IncorrectedFieldsException;
import system.exceptions.UserExistException;
import system.repository.RoleRepository;
import system.repository.TransactionRepository;
import system.repository.UserRepository;
import system.service.AccountService;
import system.service.UserService;
import system.utils.CryptoUtils;

import java.util.*;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

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

    @Override
    public boolean checkFieldsBeforeCreate(String email, String login, String documentNumber, String first_name, String last_name, String middle_name, String password, String phone) throws IncorrectedFieldsException {

        //TODO тут написать логику для проверки заполнености и правильности полей
        return true;
    }

    @Override
    public List<Action> findLastActions(User user) {

        List<Action> actions = new ArrayList<Action>();
        TreeMap<Date, Transaction > mapa = new TreeMap<Date, Transaction>(Collections.reverseOrder());
        List<Account> accounts = user.getAccountList();
        for (Account account : accounts)
        {
            for (Transaction oper : transactionRepository.findFirst10Operation(account.getId()))
            {
                mapa.put(oper.getDate(), oper);
            }
        }
        int count = 0;

        for (Map.Entry<Date, Transaction> entry : mapa.entrySet()) {
            if (count >= 8) break;
            actions.add(new Action(entry.getValue().getType(), entry.getValue().getDescription(), new java.sql.Date(entry.getValue().getDate().getTime()).toLocalDate()));
            count++;
        }
        return actions;

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