package system.service.implemention;

import system.entity.Account;
import system.entity.Transaction;
import system.service.AccountExporterService;

import java.util.*;

public class AccountExporterImpl implements AccountExporterService {

    @Override
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
