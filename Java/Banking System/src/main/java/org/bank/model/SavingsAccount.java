package org.bank.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a savings account within the banking system.
 * A savings account has an interest rate and a limit on the number of withdrawals allowed per month.
 */
public class SavingsAccount extends Account {
    private double interestRate;
    private int monthlyWithdrawalLimit = 3;
    private int withdrawalsThisMonth = 0;

    /**
     * Constructs a SavingsAccount instance with specified owners, balance, and interest rate.
     *
     * @param owner        the list of users who own the account
     * @param balance      the initial balance of the savings account
     * @param interestRate the interest rate applied to the savings account
     */
    public SavingsAccount(List<User> owner, double balance, double interestRate) {
        super(owner, balance);
        this.interestRate = interestRate;
    }

    /**
     * Retrieves the interest rate for this savings account.
     *
     * @return the interest rate of the account
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Sets the interest rate for this savings account.
     *
     * @param interestRate the interest rate to be set
     */
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Returns a string representation of the savings account, including interest rate and withdrawal limits.
     *
     * @return a string representation of the savings account
     */
    @Override
    public String toString() {
        return String.format("SavingsAccount | %s | interestRate=%.2f | monthlyWithdrawalLimit=%d | withdrawalsThisMonth=%d",
                super.toString(), interestRate, monthlyWithdrawalLimit, withdrawalsThisMonth);
    }

}
