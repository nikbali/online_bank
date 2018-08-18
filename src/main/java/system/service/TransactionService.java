package system.service;

import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    void save(Transaction transaction);
    Transaction deposit(Account account , double amount);
    Optional<Transaction> findById(long id);
    boolean existsById(long id);
    void deleteById(long id);
    void deleteAll();
    long count();

}
