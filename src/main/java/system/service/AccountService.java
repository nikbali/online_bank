package system.service;

import system.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    void save(Account account);
    Account createAccount();
    Optional<Account> findById(long id);
    boolean existsById(long id);
    void deleteById(long id);
    void delete(Account account);
    void deleteAll();
    long count();
    List<Account> loadAll();
}
