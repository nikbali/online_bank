package system.service.implemention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.entity.Transaction;
import system.repository.AccountRepository;
import system.repository.TransactionRepository;
import system.service.TransactionService;
import system.service.UserService;

import java.util.Date;
import java.util.Optional;
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Override
    public void save(Transaction transaction) {

    }

    @Override
    public Transaction deposit(Account account, double amount) {
        if(amount > 0 && accountRepository.existsByAccountNumber(account.getAccountNumber()))
        {
            account.setAccount_balance(amount + account.getAccount_balance());
            accountRepository.save(account);
            Transaction transaction = new Transaction(amount, new Date(), "Deposit "+ amount+ " USD", "Done", "Deposit", account, account);
            transactionRepository.save(transaction);
            return transaction;
        }
        return null;
    }

    @Override
    public Optional<Transaction> findById(long id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public long count() {
        return 0;
    }
}
