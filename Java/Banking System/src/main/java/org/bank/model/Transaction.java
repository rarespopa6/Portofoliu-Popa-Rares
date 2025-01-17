package org.bank.model;

import java.util.Date;

/**
 * Represents a transaction between two accounts in the banking system.
 */
public class Transaction implements Identifiable {
    private int id;
    private Account sourceAccount;
    private Account destinationAccount;
    private double amount;
    private Date date;

    /**
     * @param sourceAccount the account from which the money is withdrawn
     * @param destinationAccount the account to which the money is deposited
     * @param amount the amount of money transferred
     */
    public Transaction(Account sourceAccount, Account destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.date = new Date();
    }

    /**
     * @return the unique identifier of the transaction
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the unique identifier of the transaction to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the account from which the money is withdrawn
     */
    public Account getSourceAccount() {
        return sourceAccount;
    }

    /**
     * @param sourceAccount the account from which the money is withdrawn
     */
    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    /**
     * @return the account to which the money is deposited
     */
    public Account getDestinationAccount() {
        return destinationAccount;
    }

    /**
     * @param destinationAccount the account to which the money is deposited
     */
    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    /**
     * @return the amount of money transferred
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount of money transferred
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the date and time when the transaction was made
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date and time when the transaction was made
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return a string representation of the Transaction object, including the source account,
     * destination account, amount, and date.
     */
    @Override
    public String toString() {
        return "Transaction | " +
                "id=" + id +
                "| sourceAccount=" + sourceAccount.getId() +
                "| destinationAccount=" + destinationAccount.getId() +
                "| amount=" + amount +
                "| date=" + date;
    }
}
