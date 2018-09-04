package system.service.implemention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;
import system.enums.Bank;
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
        if(amount > 0 && sender!=null && receiver!=null && sender.getAccount_balance() >= amount && sender != receiver)
        {
            sender.setAccount_balance(sender.getAccount_balance() - amount);
            accountRepository.save(sender);
            receiver.setAccount_balance(receiver.getAccount_balance() + amount);
            accountRepository.save(receiver);
            Transaction transaction = new Transaction(amount, new Date(), "Transfer "+ amount+ " "+receiver.getCurrency(), "Done", "TransferToOurBank", sender, receiver);
            transactionRepository.save(transaction);
            return transaction;
        }
        return null;
    }


    @Override
    public Transaction transferToOtherBank(Account sender, Account receiver, double amount, String comment) {
        if(amount > 0 && sender!=null && receiver!=null && sender != receiver && receiver.getCurrency() == sender.getCurrency() )
        {
            receiver.setAccount_balance(receiver.getAccount_balance() + amount);
            accountRepository.save(receiver);
            accountRepository.save(sender);
            Transaction transaction = new Transaction(amount, new Date(), comment, "Done", "TransferToOtherBank", sender, receiver);
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
