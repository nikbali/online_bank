package system.service.implemention;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.entity.User;
import system.enums.AccountType;
import system.enums.Currency;
import system.repository.AccountRepository;
import system.service.AccountService;
import system.service.UserService;

import java.math.BigDecimal;
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

        long accountNumber = RandomUtils.nextLong(10000000, 100000000);
        String accountNumberString = "10" + Long.toString(accountNumber);
        accountNumber = Long.parseLong(accountNumberString);

        while (accountRepository.existsByAccountNumber(accountNumber))
        {
            accountNumber = RandomUtils.nextLong(10000000, 100000000);
            accountNumberString = "10" + Long.toString(accountNumber);
            accountNumber = Long.parseLong(accountNumberString);
        }
        account.setAccount_balance(BigDecimal.ZERO);
        account.setAccountNumber(accountNumber);
        account.setUser(user);
        account.setCurrency(Currency.RUB);
        account.setType(AccountType.DEBIT);
        account.setBic(100);
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
