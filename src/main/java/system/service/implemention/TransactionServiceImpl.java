package system.service.implemention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;
import system.repository.AccountRepository;
import system.repository.TransactionRepository;
import system.service.TransactionService;
import system.service.UserService;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;
import java.security.Principal;
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
        if(amount > 0 && account!=null)
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
    public Transaction transfer(Account sender, Account receiver, double amount){
        if(amount > 0 && sender!=null && receiver!=null && sender.getAccount_balance() >= amount && sender.getAccountNumber() != receiver.getAccountNumber() )
        {
            sender.setAccount_balance(sender.getAccount_balance() - amount);
            accountRepository.save(sender);
            receiver.setAccount_balance(receiver.getAccount_balance() + amount);
            accountRepository.save(receiver);
            Transaction transaction = new Transaction(amount, new Date(), "Transfer "+ amount+ " RUB", "Done", "Transfer", sender, receiver);
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
