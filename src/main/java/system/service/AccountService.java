package system.service;

import org.springframework.data.jpa.repository.Query;
import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    void save(Account account);
    Account createAccount(User user);
    Optional<Account> findById(long id);
    Account findByAccountNumber(long accountNumber);

    boolean existsById(long id);
    void deleteById(long id);
    void delete(Account account);
    void deleteAll();
    long count();
    List<Account> loadAll();

}
