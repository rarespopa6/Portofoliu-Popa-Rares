package org.bank.model.mapper;

import org.bank.model.CheckingAccount;
import org.bank.model.Loan;
import org.bank.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper class for mapping a {@link ResultSet} to a {@link Transaction} object.
 */
public class TransactionMapper implements Mapper<Transaction> {
    /**
     * Maps a {@link ResultSet} to a {@link Transaction} object.
     * @param resultSet The {@link ResultSet} containing the transaction data from the database.
     * @return A {@link Transaction} object, populated with data from the {@link ResultSet}.
     * @throws SQLException If there is an error accessing the data in the {@link ResultSet}.
     * @throws IllegalArgumentException If an unknown user type is encountered in the {@link ResultSet}.
     */
    @Override
    public Transaction map(ResultSet resultSet) throws SQLException {
        Transaction transaction;
        CheckingAccount sourceAccount;
        CheckingAccount destinationAccount;

        sourceAccount = new CheckingAccount(resultSet.getInt("source_account_id"));
        destinationAccount = new CheckingAccount(resultSet.getInt("destination_account_id"));

        try {
            transaction = new Transaction(
                    sourceAccount,
                    destinationAccount,
                    resultSet.getDouble("amount")
            );

            transaction.setId(resultSet.getInt("id"));
            transaction.setDate(resultSet.getDate("transaction_date"));

        } catch (Exception e) {
            throw new IllegalArgumentException("Error mapping transaction: " + e.getMessage());
        }

        return transaction;
    }
}
