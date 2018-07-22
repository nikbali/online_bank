package system.repository;

import org.springframework.data.repository.CrudRepository;
import system.entity.Transaction;
import system.entity.User;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
