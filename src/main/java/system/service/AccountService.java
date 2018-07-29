package system.service;

import system.entity.Account;

import java.util.List;

public interface AccountService {
    void save(Account account);

    Account createAccount(Account account);//save and create whats the diff ar because of logic of creation?

    Account findById(long id);

    boolean existsById(long id);

    Iterable<Account> findAll();

    void deleteById(long id);

    void delete(Account account);

    void deleteAll(); //delete all accounts of specified user

    long count();

    List<Account> loadAll();

}
