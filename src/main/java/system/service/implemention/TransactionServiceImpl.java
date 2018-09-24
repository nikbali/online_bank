package system.service.implemention;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.entity.Action;
import system.entity.Transaction;
import system.entity.User;
import system.enums.Bank;
import system.enums.StatusOperation;
import system.enums.TypeOperation;
import system.repository.AccountRepository;
import system.repository.TransactionRepository;
import system.service.AccountExporterService;
import system.service.TransactionService;
import system.service.UserService;
import system.utils.UserUtils;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
import java.util.*;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Override
    @Transactional
    public void save(Transaction transaction) {
    }

    @Override
    @Transactional
    public Transaction deposit(Account account, BigDecimal amount) {
        LOG.info("Пополнение счета {} на {} {}  Status: {}", account.getAccountNumber(), amount, account.getCurrency() , StatusOperation.IN_PROGRESS.getName());
        if(amount.compareTo(BigDecimal.ZERO) > 0 && account != null)
        {
            account.setAccount_balance(account.getAccount_balance().add(amount));
            accountRepository.save(account);
            Transaction transaction = new Transaction(
                    amount,
                    Instant.now(),
                    "Deposit " + amount + " " + account.getCurrency().name(),
                    StatusOperation.DONE,
                    TypeOperation.DEPOSIT,
                    account,
                    account);
            transactionRepository.save(transaction);
            LOG.info("Пополнение счета {} на {} {}  Status: {}", account.getAccountNumber(), amount, account.getCurrency() , StatusOperation.DONE.getName());
            return transaction;
        }
        LOG.info("Пополнение счета {} на {} {}  Status: {}", account.getAccountNumber(), amount, account.getCurrency() , StatusOperation.ERROR.getName());
        return null;
    }

    @Override
    @Transactional
    public Transaction transfer(Account sender, Account receiver, BigDecimal amount){
        LOG.info("Перевод {} {} от {} к {} Status: {}", amount, sender.getCurrency(), sender.getAccountNumber(), receiver.getAccountNumber(), StatusOperation.IN_PROGRESS.getName());
        if(amount.compareTo(BigDecimal.ZERO) > 0 &&
                sender!=null && receiver!=null &&
                sender.getAccount_balance().compareTo(amount) >= 0 &&
                sender.getAccountNumber() != receiver.getAccountNumber() &&
                receiver.getCurrency() == sender.getCurrency())
        {
            sender.setAccount_balance(sender.getAccount_balance().subtract(amount));
            accountRepository.save(sender);
            receiver.setAccount_balance(receiver.getAccount_balance().add(amount));
            accountRepository.save(receiver);
            Transaction transaction = new Transaction(
                    amount,
                    Instant.now(),
                    "Transfer "+ amount+ " "+receiver.getCurrency(),
                    StatusOperation.DONE,
                    TypeOperation.TRANSFER,
                    sender,
                    receiver);
            transactionRepository.save(transaction);
            LOG.info("Перевод {} {} от {} к {} Status: {}", amount, sender.getCurrency(), sender.getAccountNumber(), receiver.getAccountNumber(), StatusOperation.DONE.getName());
            return transaction;
        }
        LOG.info("Перевод {} {} от {} к {} Status: {}", amount, sender.getCurrency(), sender.getAccountNumber(), receiver.getAccountNumber(), StatusOperation.ERROR.getName());
        return null;
    }

    @Override
    @Transactional
    public Transaction transferFromOtherBank(Account sender, Account receiver, BigDecimal amount, String comment) {
        LOG.info("Перевод из другого банка {} {} от {} к {} Status: {}", amount, sender.getCurrency(), sender.getAccountNumber(), receiver.getAccountNumber(), StatusOperation.IN_PROGRESS.getName());
        if(amount.compareTo(BigDecimal.ZERO) > 0 &&
                sender!=null && receiver!=null &&
                sender != receiver &&
                receiver.getCurrency() == sender.getCurrency() )
        {
            receiver.setAccount_balance(receiver.getAccount_balance().add(amount));
            accountRepository.save(receiver);
            accountRepository.save(sender);
            Transaction transaction = new Transaction(
                    amount,
                    Instant.now(),
                    comment,
                    StatusOperation.DONE,
                    TypeOperation.TRANSFER_FROM_ANOTHER_BANK,
                    sender,
                    receiver);
            transactionRepository.save(transaction);
            LOG.info("Перевод из другого банка {} {} от {} к {} Status: {}", amount, sender.getCurrency(), sender.getAccountNumber(), receiver.getAccountNumber(), StatusOperation.DONE.getName());
            return transaction;
        }
        LOG.info("Перевод из другого банка {} {} от {} к {} Status: {}", amount, sender.getCurrency(), sender.getAccountNumber(), receiver.getAccountNumber(), StatusOperation.ERROR.getName());
        return null;
    }

    @Override
    public Optional<Transaction> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Transaction> loadAllTransactionByUser(User user) {
        List<Transaction> transactions = new ArrayList<>();
        AccountExporterService accountExporterService = new AccountExporterImpl();
        List<Account> accounts = user.getAccountList();
        for(Account account : accounts){
            transactions.addAll(accountExporterService.exportAccountHistory(account));
        }
        return transactions;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> loadAllTransactions() {
        return  Lists.newArrayList(transactionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> loadAllTransactionsForRangeDate(Date from, Date to) {
        NavigableMap<Instant, Transaction> mapa = new TreeMap<Instant, Transaction>();
        for (Transaction oper : transactionRepository.findAll()) {
            mapa.put(oper.getDate(), oper);
        }
        int count = 0;
        List<Transaction> operations = new ArrayList<Transaction>(mapa.subMap(from.toInstant(), true, to.toInstant(), true).values());
        return operations;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> loadAllTransactionsForRangeDateByUser(User user, Date from, Date to) {
        NavigableMap<Instant, Transaction> mapa = new TreeMap<Instant, Transaction>();
        for (Transaction oper : loadAllTransactionByUser(user)) {
            mapa.put(oper.getDate(), oper);
        }
        List<Transaction> operations = new ArrayList<Transaction>();
        SortedMap<Instant, Transaction> map = mapa.subMap(from.toInstant(), to.toInstant());
        for (Map.Entry<Instant, Transaction> entry : map.entrySet()) operations.add(entry.getValue());
        return operations;
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

    @Override
    @Transactional
    public Transaction transferToOtherBank(Account sender, Account receiver, BigDecimal amount) {
        LOG.info("Перевод в другой банка {} {} от {} к {} Status: {}", amount, sender.getCurrency(), sender.getAccountNumber(), receiver.getAccountNumber(), StatusOperation.IN_PROGRESS.getName());
        if(amount.compareTo(BigDecimal.ZERO) > 0 &&
                sender!=null && receiver!=null &&
                sender != receiver &&
                receiver.getCurrency() == sender.getCurrency() )
        {
            sender.setAccount_balance(sender.getAccount_balance().subtract(amount));
            accountRepository.save(receiver);
            accountRepository.save(sender);
            Transaction transaction = new Transaction(
                    amount,
                    Instant.now(),
                    "Перевод в другой банк " + amount.doubleValue() + sender.getCurrency().name(),
                    StatusOperation.DONE,
                    TypeOperation.TRANSFER_TO_ANOTHER_BANK,
                    sender,
                    receiver);
            transactionRepository.save(transaction);
            LOG.info("Перевод в другой банк {} {} от {} к {} Status: {}", amount, sender.getCurrency(), sender.getAccountNumber(), receiver.getAccountNumber(), StatusOperation.DONE.getName());
            return transaction;
        }
        LOG.info("Перевод в другой банк {} {} от {} к {} Status: {}", amount, sender.getCurrency(), sender.getAccountNumber(), receiver.getAccountNumber(), StatusOperation.ERROR.getName());
        return null;
    }
}
