package system.service.implemention;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.entity.User;
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


    @Override
    public Account createAccount(User user) {
        Account account = new Account();

        long accountNumber = RandomUtils.nextLong(1000000, 1000000000);
        while (accountRepository.existsByAccountNumber(accountNumber))
        {
            accountNumber = RandomUtils.nextLong(1000000, 1000000000);
        }
        account.setAccount_balance(0.0);
        account.setAccountNumber(accountNumber);
        account.setUser(user);
        accountRepository.save(account);
        return accountRepository.findByAccountNumber(account.getAccountNumber());
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
    public Account findByAccountNumber(long accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
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