package system.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import system.entity.Account;
import system.entity.Transaction;
import system.entity.User;

import java.util.List;

public interface AccountRepository  extends CrudRepository<Account, Long> {
    List<Account> findAll();
    Account findByAccountNumber(long accountNumber);
    boolean existsByAccountNumber(long accountNumber);

}
