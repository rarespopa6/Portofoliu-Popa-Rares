package org.bank.model.mapper;

import org.bank.model.Customer;
import org.bank.model.Loan;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper class for mapping a {@link ResultSet} to a {@link Loan} object.
 */
public class LoanMapper implements Mapper<Loan> {
    /**
     * Maps a {@link ResultSet} to a {@link Loan} object.
     * @param resultSet The {@link ResultSet} containing the loan data from the database.
     * @return A {@link Loan} object, populated with data from the {@link ResultSet}.
     * @throws SQLException If there is an error accessing the data in the {@link ResultSet}.
     * @throws IllegalArgumentException If an unknown user type is encountered in the {@link ResultSet}.
     */
    @Override
    public Loan map(ResultSet resultSet) throws SQLException {
        Loan loan;
        Customer customer;

        customer = new Customer(resultSet.getInt("borrower_id"));

        try {
            loan = new Loan(
                    customer,
                    resultSet.getDouble("loan_amount"),
                    resultSet.getInt("term_months")
            );

            loan.setId(resultSet.getInt("id"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error mapping loan: " + e.getMessage());
        }

        return loan;
    }
}
