package system.service.implemention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.repository.UserRepository;
import system.service.AccountService;
import system.service.UserService;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);


    @Override
    public void save(Account account) {

    }

    @Override
    public Account createAccount(Account account) {
        return null;
    }

    @Override
    public Account findById(long id) {
        return null;
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public Iterable<Account> findAll() {
        return null;
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void delete(Account account) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<Account> loadAll() {
        return null;
    }
}
