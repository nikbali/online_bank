package system.service.implemention;

import system.entity.Account;
import system.entity.Transaction;
import system.service.AccountExporterService;

import java.util.ArrayList;
import java.util.List;

public class AccountExporterImpl implements AccountExporterService {

    @Override
    public List<Transaction> exportAccountHistory(Account account) {
        List<Transaction> history = new ArrayList<>();
        history.addAll(account.getSendList());
        history.addAll(account.getRecieveList());
        return history;
    }
}
