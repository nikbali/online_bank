package system.service;

import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    void save(Transaction transaction);
    Transaction deposit(Account account , BigDecimal amount);
    Transaction transfer(Account sender, Account receiver, BigDecimal amount);
    Transaction transferFromOtherBank(Account sender, Account receiver, BigDecimal amount, String comment);
    Optional<Transaction> findById(long id);
    boolean existsById(long id);
    void deleteById(long id);
    void deleteAll();
    long count();

}
