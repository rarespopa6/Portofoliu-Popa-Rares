package org.bank.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a credit card within the banking system.
 * Each credit card is associated with a user and an account.
 */
public class CreditCard {
    private long cardId;
    private User owner;
    private Account account;

    private double cardLimit;
    private double currentBalance;
    private double interestRate;

    private long cardNumber;
    private LocalDate expiryDate;
    private int cvv;


    /**
     * Default constructor for CreditCard.
     */
    public CreditCard() {
    }

    /**
     * Constructs a CreditCard instance with specified details.
     *
     * @param cardId         the unique identifier of the credit card
     * @param owner          the user who owns the credit card
     * @param account        the account linked to the credit card
     * @param cardLimit      the limit of the credit card
     * @param currentBalance  the current balance of the credit card
     * @param interestRate    the interest rate applicable to the credit card
     * @param cardNumber     the unique card number
     * @param expiryDate     the expiry date of the credit card
     * @param cvv            the card verification value
     */
    public CreditCard(long cardId, User owner, Account account, double cardLimit, double currentBalance, double interestRate, long cardNumber, LocalDate expiryDate, int cvv) {
        this.cardId = cardId;
        this.owner = owner;
        this.account = account;
        this.cardLimit = cardLimit;
        this.currentBalance = currentBalance;
        this.interestRate = interestRate;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    /**
     * Retrieves the unique identifier of the credit card.
     *
     * @return the card ID
     */
    public long getCardId() {
        return cardId;
    }

    /**
     * Sets the unique identifier of the credit card.
     *
     * @param cardId the card ID to set
     */
    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    /**
     * Retrieves the user who owns the credit card.
     *
     * @return the owner of the credit card
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets the user who owns the credit card.
     *
     * @param owner the user to set as owner
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Retrieves the account linked to the credit card.
     *
     * @return the account associated with the credit card
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Sets the account linked to the credit card.
     *
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Retrieves the credit limit of the card.
     *
     * @return the card limit
     */
    public double getCardLimit() {
        return cardLimit;
    }

    /**
     * Sets the credit limit of the card.
     *
     * @param cardLimit the limit to set
     */
    public void setCardLimit(double cardLimit) {
        this.cardLimit = cardLimit;
    }

    /**
     * Retrieves the current balance on the card.
     *
     * @return the current balance
     */
    public double getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Sets the current balance on the card.
     *
     * @param currentBalance the balance to set
     */
    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    /**
     * Retrieves the interest rate applicable to the credit card.
     *
     * @return the interest rate
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Sets the interest rate applicable to the credit card.
     *
     * @param interestRate the interest rate to set
     */
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Retrieves the unique card number.
     *
     * @return the card number
     */
    public long getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the unique card number.
     *
     * @param cardNumber the card number to set
     */
    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Retrieves the expiry date of the credit card.
     *
     * @return the expiry date
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiry date of the credit card.
     *
     * @param expiryDate the expiry date to set
     */
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Retrieves the card verification value (CVV).
     *
     * @return the CVV
     */
    public int getCvv() {
        return cvv;
    }

    /**
     * Sets the card verification value (CVV).
     *
     * @param cvv the CVV to set
     */
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    /**
     * Returns a string representation of the CreditCard object, including all details.
     *
     * @return a string representation of the credit card
     */
    @Override
    public String toString() {
        return "CreditCard | " +
                "cardId=" + cardId +
                "| owner=" + owner +
                "| account=" + account +
                "| cardLimit=" + cardLimit +
                "| currentBalance=" + currentBalance +
                "| interestRate=" + interestRate +
                "| cardNumber=" + cardNumber +
                "| expiryDate=" + expiryDate +
                "| cvv=" + cvv;
    }
}
