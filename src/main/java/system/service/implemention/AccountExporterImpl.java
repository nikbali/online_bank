package system.service.implemention;

import org.springframework.transaction.annotation.Transactional;
import system.entity.Account;
import system.entity.Transaction;
import system.service.AccountExporterService;

import java.util.*;

public class AccountExporterImpl implements AccountExporterService {

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> exportAccountHistory(Account account) {
        List<Transaction> history = new ArrayList<>();
        history.addAll(account.getSendList());

        for (Transaction transaction : account.getRecieveList()) {
            if(!history.contains(transaction)){
                history.add(transaction);
            }
        }
        return history;
    }

}
