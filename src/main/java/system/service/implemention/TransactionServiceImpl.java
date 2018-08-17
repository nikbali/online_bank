package system.service.implemention;

import system.entity.Account;
import system.entity.Transaction;
import system.service.TransactionService;

import java.util.Optional;

public class TransactionServiceImpl implements TransactionService {
    @Override
    public void save(Transaction transaction) {

    }

    @Override
    public Transaction deposit(Account account, double amount) {
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
