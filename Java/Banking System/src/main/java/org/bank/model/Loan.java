package org.bank.model;


/**
 * Represents a loan that a customer can take out from the bank.
 * Requires a borrower, loan amount, interest rate, and term in months.
 */
public class Loan implements Identifiable {
    private int id;
    private Customer borrower;
    private double loanAmount;
    private static double interestRate = 0.05;
    private int termMonths;

    /**
     * @param borrower the customer who is taking out the loan
     * @param loanAmount the amount of the loan
     * @param termMonths the term of the loan in months
     */
    public Loan(Customer borrower, double loanAmount, int termMonths) {
        this.borrower = borrower;
        this.loanAmount = loanAmount;
        this.termMonths = termMonths;
    }

    /**
     * @return the unique identifier of the loan
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the customer who is taking out the loan
     */
    public Customer getBorrower() {
        return borrower;
    }

    /**
     * @param borrower the customer to set as the borrower
     */
    public void setBorrower(Customer borrower) {
        this.borrower = borrower;
    }

    /**
     * @return the amount of the loan
     */
    public double getLoanAmount() {
        return loanAmount;
    }

    /**
     * @param amount the amount of the loan to set
     */
    public void setLoanAmount(double amount) {
        this.loanAmount = amount;
    }

    /**
     * @return the interest rate of the loan
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * @param interestRate the interest rate to set
     * @throws IllegalArgumentException if the interest rate is negative
     */
    public void setInterestRate(double interestRate) {
        if(interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }

        this.interestRate = interestRate;
    }

    /**
     * @return the term of the loan in months
     */
    public int getTermMonths() {
        return termMonths;
    }

    /**
     * @param termMonths the term of the loan to set in months
     */
    public void setTermMonths(int termMonths) {
        this.termMonths = termMonths;
    }

    /**
     * @return the calculated monthly payment amount for the loan
     */
    public double calculateMonthlyPayment() {
        double monthlyInterestRate = interestRate / 12;
        return this.loanAmount * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, - this.termMonths));
    }

    /**
     * @return a string representation of the loan
     */
    @Override
    public String toString() {
        return "Loan | " +
                "id=" + id +
                "| borrower=" + borrower.getId() +
                "| loanAmount=" + loanAmount +
                "| termMonths=" + termMonths;
    }
}
