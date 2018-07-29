package system.repository;

import org.springframework.data.repository.CrudRepository;
import system.entity.Account;
import system.entity.User;

public interface AccountRepository  extends CrudRepository<Account, Long> {




}
