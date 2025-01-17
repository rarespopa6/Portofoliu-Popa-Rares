package org.bank.model.mapper;

import org.bank.model.Account;
import org.bank.model.CheckingAccount;
import org.bank.model.SavingsAccount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The AccountMapper class implements the Mapper interface to map the result set from the database
 * to an Account object. It supports mapping of different account types, such as CheckingAccount and SavingsAccount.
 */
public class AccountMapper implements Mapper<Account> {

    /**
     * Maps the result set from the database to an Account object.
     * This method is responsible for determining the type of account (Checking or Savings)
     * and constructing the appropriate account object.
     *
     * @param resultSet The ResultSet from the database query.
     * @return The Account object populated with data from the result set.
     * @throws SQLException If there is an error accessing the result set.
     * @throws IllegalArgumentException If the account type is unknown.
     */
    @Override
    public Account map(ResultSet resultSet) throws SQLException {
        String accountType = resultSet.getString("type");
        Account account;

        if ("CHECKING".equalsIgnoreCase(accountType)) {
            account = new CheckingAccount(
                    new ArrayList<>(),
                    resultSet.getDouble("balance"),
                    resultSet.getDouble("transaction_fee")
            );
        } else if ("SAVINGS".equalsIgnoreCase(accountType)) {
            account = new SavingsAccount(
                    new ArrayList<>(),
                    resultSet.getDouble("balance"),
                    resultSet.getDouble("interest_rate")
            );
        } else {
            throw new IllegalArgumentException("Unknown account type: " + accountType);
        }

        account.setId(resultSet.getInt("id"));
        account.setCreationTime(resultSet.getTimestamp("creation_time").toLocalDateTime());

        return account;
    }
}
