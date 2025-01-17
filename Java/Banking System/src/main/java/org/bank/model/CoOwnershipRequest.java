package org.bank.model;

/**
 * Represents a co-ownership request for a bank account.
 * This request is made by a requester to gain ownership of a specific account.
 * The request will be approved or denied by the account owner.
 */
public class CoOwnershipRequest implements Identifiable {
    private int id;
    private Account account;
    private Customer requester;
    private Customer accountOwner;
    private boolean approved;

    /**
     * Constructs a CoOwnershipRequest instance with specified account, requester, and account owner.
     * Initially, the request is marked as not approved.
     *
     * @param account       the account for which co-ownership is requested
     * @param requester     the customer who is requesting co-ownership
     * @param accountOwner  the customer who owns the account
     */
    public CoOwnershipRequest(Account account, Customer requester, Customer accountOwner) {
        this.id = id;
        this.account = account;
        this.requester = requester;
        this.accountOwner = accountOwner;
        this.approved = false;
    }

    /**
     * Retrieves the unique identifier of the co-ownership request.
     *
     * @return the request ID
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the co-ownership request.
     *
     * @param id the request ID to set
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the account associated with the co-ownership request.
     *
     * @return the account for which co-ownership is requested
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Retrieves the customer who is requesting co-ownership of the account.
     *
     * @return the customer making the request
     */
    public Customer getRequester() {
        return requester;
    }

    /**
     * Retrieves the customer who owns the account that is being requested for co-ownership.
     *
     * @return the account owner
     */
    public Customer getAccountOwner() {
        return accountOwner;
    }

    /**
     * Retrieves the approval status of the co-ownership request.
     *
     * @return true if the request is approved, false otherwise
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Sets the approval status of the co-ownership request.
     *
     * @param approved true if the request is approved, false otherwise
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * Returns a string representation of the CoOwnershipRequest object, including its ID,
     * the associated account, requester, and approval status.
     *
     * @return a string representation of the co-ownership request
     */
    @Override
    public String toString() {
        return "Co-Ownership Request ID: " + id + ", Account: " + account.getId() +
                ", Requester: " + requester.getId() + ", Approved: " + approved;
    }
}
