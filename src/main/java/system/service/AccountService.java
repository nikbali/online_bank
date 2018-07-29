package system.service;

import system.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    void save(Account account);

    Account createAccount(Account account);

    Optional<Account> findById(long id);

    boolean existsById(long id);

    void deleteById(long id);

    void delete(Account account);

    void deleteAll(); //delete all accounts of specified user

    long count();

    List<Account> loadAll();

}
