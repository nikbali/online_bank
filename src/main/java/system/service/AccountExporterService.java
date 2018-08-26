package system.service;

import system.entity.Account;
import system.entity.Transaction;

import java.util.List;

public interface AccountExporterService {

    List<Transaction> exportAccountHistory(Account account);
}
