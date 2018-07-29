package system.service.implemention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.repository.AccountRepository;
import system.service.AccountService;
import system.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    //реализовать создине объекта, добавить проверки
    @Override
    public Account createAccount(Account account) {
        return null;
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }


    @Override
    public Optional<Account> findById(long id) {
        return accountRepository.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return accountRepository.existsById(id);
    }

    @Override
    public void deleteById(long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public void delete(Account account) {
        accountRepository.delete(account);
    }

    @Override
    public void deleteAll() {
        accountRepository.deleteAll();
    }

    @Override
    public long count() {
        return accountRepository.count();
    }

    @Override
    public List<Account> loadAll() {
        return accountRepository.findAll();
    }
}
