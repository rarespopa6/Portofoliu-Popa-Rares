package org.bank.model;

import java.util.List;

public class Bank {
    private static Bank instance = null;

    private String name;
    private List<Account> accounts;
    private List<Customer> customers;
    private List<Employee> employees;
    private List<Loan> loans;


    /**
     * Implemented the singleton pattern to ensure that only one instance of the Bank class is created
     *
     * @param name name of the bank
     * @param accounts list of accounts
     * @param customers list of customers
     * @param employees list of employees
     * @param loans list of loans
     */
    private Bank(String name, List<Account> accounts, List<Customer> customers, List<Employee> employees, List<Loan> loans) {
        this.name = name;
        this.accounts = accounts;
        this.customers = customers;
        this.employees = employees;
        this.loans = loans;
    }

    /**
     * @param name name of the bank
     * @param accounts list of accounts
     * @param customers list of customers
     * @param employees list of employees
     * @param loans list of loans
     * @return instance of the Bank class
     */
    public static Bank getInstance(String name, List<Account> accounts, List<Customer> customers, List<Employee> employees, List<Loan> loans) {
        if (instance == null) {
            instance = new Bank(name, accounts, customers, employees, loans);
        }

        return instance;
    }

    /**
     * @return name of the bank
     */
    public String getName() {
        return name;
    }

    /**
     * @param name name of the bank
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return list of accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts list of accounts
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * @return list of customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * @param customers list of customers
     */
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    /**
     * @return list of employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * @param employees list of employees
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    /**
     * @return list of loans
     */
    public List<Loan> getLoans() {
        return loans;
    }

    /**
     * @param loans list of loans
     */
    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    /**
     * @return string representation of the Bank class
     */
    @Override
    public String toString() {
        return "Bank | " +
                "name='" + name + '\'' +
                "| accounts=" + accounts +
                "| customers=" + customers +
                "| employees=" + employees +
                "| loans=" + loans;
    }
}
