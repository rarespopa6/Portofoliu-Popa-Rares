package org.bank.model.mapper;

import org.bank.model.Account;
import org.bank.model.AccountLogs;
import org.bank.model.CheckingAccount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Mapper class for mapping a {@link ResultSet} to an {@link AccountLogs} object.
 */
public class AccountLogsMapper implements Mapper<AccountLogs> {

    @Override
    public AccountLogs map(ResultSet resultSet) throws SQLException {
        AccountLogs accountLogs = null;
        Account previousAccount = null;

        do {
            int accountId = resultSet.getInt("account_id");
            String message = resultSet.getString("message");

            if (previousAccount == null || previousAccount.getId() != accountId) {
                if (accountLogs != null) {
                    break;
                }
                Account account = new CheckingAccount(accountId);
                accountLogs = new AccountLogs(account);
            }

            accountLogs.addLog(message);
            previousAccount = accountLogs.getAccount();
        } while (resultSet.next());

        return accountLogs;
    }
}



