package org.bank.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a storage of logs for a specific account.
 */
public class AccountLogs implements Identifiable {
    private Account account;
    private List<String> logs;

    /**
     * @param account the account for which the logs are stored
     */
    public AccountLogs(Account account) {
        this.account = account;
        this.logs = new ArrayList<>();
    }

    /**
     * @return the list of logs
     */
    public List<String> getLogs() {
        return logs;
    }

    /**
     * @param logs the list of logs to set
     */
    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    /**
     * Adds a message to logs
     */
    public void addLog(String message) {
        logs.add(message);
    }

    /**
     * @return the account for which the logs are stored
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the new account for which the logs are stored
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @param destinationAccount the account to which the money is transferred
     * @param amount the amount of money transferred
     */
    public void addTransactionLog(Account destinationAccount, double amount) {
        logs.add("Transaction: " + account.getId() + " -> " + destinationAccount.getId() + " : " + amount);
    }

    /**
     * @param amount the amount of money deposited
     */
    public void addDepositLog(double amount) {
        logs.add("Deposit: " + amount);
    }

    /**
     * @param amount the amount of money withdrawn
     */
    public void addWithdrawLog(double amount) {
        logs.add("Withdraw: " + amount);
    }

    /**
     * @return a string representation of the AccountLogs object, including details about the account and logs.
     */
    @Override
    public String toString() {
        return "AccountLogs{" +
                "account=" + account.getId() +
                ", logs=" + logs +
                '}';
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }
}
