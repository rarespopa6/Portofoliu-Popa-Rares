package org.bank.ui;

import org.bank.controller.AppController;
import org.bank.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * UserInterface class handles the interaction with the user,
 * allowing customers and employees to perform various banking operations.
 */
public class UserInterface {
    private AppController appController;
    private final Scanner scanner = new Scanner(System.in);
    private User loggedInUser;
    private Account selectedAccount;

    /**
     * Starts the user interface and handles user interactions in a loop.
     * Provides options to log in, sign up, or exit the application.
     */
    public void start() {
        System.out.println("--- Select Data Storage Method ---");
        System.out.println("1. In-memory");
        System.out.println("2. File");
        System.out.println("3. Database");
        System.out.print("Choose an option: ");

        int storageOption;

        try {
            storageOption = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid option, defaulting to Database storage.");
            storageOption = 3;
            scanner.nextLine();
        }
        String storageMethod;

        switch (storageOption) {
            case 1:
                storageMethod = "inmemory";
                break;
            case 2:
                storageMethod = "file";
                break;
            case 3:
                storageMethod = "db";
                break;
            default:
                System.out.println("Invalid option, defaulting to Database storage.");
                storageMethod = "db";
                break;
        }

        appController = new AppController(storageMethod);

        if ("inmemory".equalsIgnoreCase(storageMethod)) {
            populateInMemoryData();
        }

        while (true) {
            System.out.println("\n--- Banking Administration System ---");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int option;

            try {
                option = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
                continue;
            }

            if (option == 0) {
                System.out.println("Exiting...");
                break;
            }

            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    signUp();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Handles user login, verifies user credentials, and directs to respective actions based on user type.
     */
    private void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();


        try {
            User user = appController.readUser(email);
            // Check if user exists based on user type
            if (user == null) {
                System.out.println("User not found.");
                return;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (!user.comparePassword(password)) {
                System.out.println("Incorrect password.");
                return;
            }

            loggedInUser = user;
            System.out.println("Logged in successfully.");

            if (loggedInUser instanceof Customer) {
                customerActions();
            } else if (loggedInUser instanceof Employee) {
                employeeActions();
            } else {
                System.out.println("Invalid user type.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Allows a new user to sign up by collecting their information and creating a customer account.
     */
    private void signUp() {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        try {
            int generatedId = appController.createCustomer(firstName, lastName, email, phoneNumber, password);
            System.out.println("Customer created with ID: " + generatedId);
            loggedInUser = appController.readUser(generatedId);
            customerActions();
        } catch (RuntimeException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays actions available to a logged-in customer and handles user choices.
     */
    private void customerActions() throws IOException {
        while (true) {
            System.out.println("\n--- Customer Actions ---");
            System.out.println("1. My Profile");
            System.out.println("2. My Accounts");
            System.out.println("3. Open New Account");
            System.out.println("4. Close Account");
            System.out.println("5. Loans");
            System.out.println("6. Manage Account Funds");
            System.out.println("7. Apply for Co-Ownership");
            System.out.println("8. View Co-Ownership Requests");
            System.out.println("9. Transactions");
            System.out.println("10. Credit Cards");
            System.out.println("11. View Account Logs");
            System.out.println("12. View Sorted Data");
            System.out.println("13. View Filtered Data");
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");

            int option;

            try {
                option = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
                continue;
            }

            if (option == 0) {
                System.out.println("Logging out...");
                loggedInUser = null; // Clear logged in user
                break;
            }

            switch (option) {
                case 1:
                    viewMyProfile();
                    break;
                case 2:
                    myAccounts();
                    break;
                case 3:
                    openNewAccount();
                    break;
                case 4:
                    closeAccount();
                    break;
                case 5:
                    setSelectedAccount();
                    loanActions();
                    break;
                case 6:
                    manageAccountFunds();
                    break;
                case 7:
                    applyForAccount();
                    break;
                case 8:
                    viewCoOwnershipRequests();
                    break;
                case 9:
                    setSelectedAccount();
                    transactionActions();
                    break;
                case 10:
                    setSelectedAccount();
                    creditCardActions();
                    break;
                case 11:
                    setSelectedAccount();
                    if(selectedAccount != null) {
                        viewLogs();
                    }
                    break;
                case 12:
                    viewSortedData();
                    break;
                case 13:
                    viewFilteredData();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /**
     * Displays actions available for credit card management and handles user choices.
     */
    private void creditCardActions() {
        while (true) {
            System.out.println("\n--- Credit Card Actions ---");
            System.out.println("1. Apply for Credit Card");
            System.out.println("2. View Credit Cards");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 0) {
                break;
            }

            switch (option) {
                case 1:
                    applyForCreditCard();
                    break;
                case 2:
                    viewCreditCards();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /**
     * Displays all credit cards associated with the logged-in customer.
     */
    private void viewCreditCards() {
        List<CreditCard> creditCards = appController.getAllCreditCardsForCustomer((Customer) loggedInUser);

        if (creditCards.isEmpty()) {
            System.out.println("No credit cards found.");
        } else {
            System.out.println("Credit Cards:");
            for (CreditCard creditCard : creditCards) {
                System.out.println(creditCard);
            }
        }
    }

    /**
     * Prompts the customer to apply for a credit card and handles the application process.
     */
    private void applyForCreditCard() {
        try {
            appController.generateCreditCardForAccount((Customer) loggedInUser, selectedAccount);
        }catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Credit card application successful.");
    }

    /**
     * Allows the customer to manage their account funds, including viewing balance, depositing, and withdrawing.
     */
    private void manageAccountFunds() {
        if (loggedInUser instanceof Customer) {
            Customer customer = (Customer) loggedInUser;
            List<Account> accounts = new ArrayList<>();

            try {
                accounts = appController.getAccountsForCustomer(customer.getId());
            } catch (IOException e){
                System.out.println("Error getting accounts for customer: " + customer.getId());
            }

            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
                return;
            }

            System.out.println("Your Accounts:");
            for (Account account : accounts) {
                System.out.println(account);
            }

            System.out.print("Enter Account ID to manage funds: ");
            int accountId = scanner.nextInt();
            scanner.nextLine();

            Account selectedAccount = accounts.stream()
                    .filter(account -> account.getId() == accountId)
                    .findFirst()
                    .orElse(null);

            if (selectedAccount == null) {
                System.out.println("Account not found.");
                return;
            }

            while (true) {
                selectedAccount = appController.getAccountById(accountId);

                System.out.println("\n--- Manage Account Funds ---");
                System.out.println("1. View Balance");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("0. Back");
                System.out.print("Choose an option: ");

                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        viewBalance(selectedAccount);
                        break;
                    case 2:
                        depositToAccount(selectedAccount);
                        break;
                    case 3:
                        withdrawFromAccount(selectedAccount);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        } else {
            System.out.println("No customer is logged in.");
        }
    }

    /**
     * Displays the balance of the selected account.
     */
    private void viewBalance(Account account) {
        System.out.println("Balance for Account ID " + account.getId() + ": " + account.getBalance());
    }

    /**
     * Handles deposit to the selected account.
     */
    private void depositToAccount(Account account) {
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        try {
            appController.depositToAccount(account.getId(), loggedInUser.getId(), amount);
            System.out.println("Deposit successful.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles withdrawal from the selected account.
     */
    private void withdrawFromAccount(Account account) {
        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        try {
            appController.withdrawFromAccount(account.getId(), loggedInUser.getId(), amount);
            System.out.println("Withdrawal successful.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Allows the logged-in user to apply for co-ownership of an account.
     * The user is prompted to enter the account ID and the account owner's email.
     * The application attempts to send a co-ownership request.
     * If the request is successful, a success message is displayed, otherwise, an error message is shown.
     */
    private void applyForAccount() {
        System.out.print("Enter account ID to request co-ownership: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter account owner's email: ");
        String accountOwnerEmail = scanner.nextLine();

        try {
            appController.applyForAccount(accountId, loggedInUser.getId(), accountOwnerEmail);
            System.out.println("Co-ownership request sent successfully.");
        } catch (RuntimeException | IOException e ) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays all pending co-ownership requests for the logged-in user.
     * If there are pending requests, the user can select a request to approve.
     * Once a request is selected, it is approved, and a success message is shown.
     * If no requests are pending, a message is displayed to inform the user.
     */
    private void viewCoOwnershipRequests() {
        List<CoOwnershipRequest> requests = appController.viewPendingRequests(loggedInUser.getId());

        if (requests.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        System.out.println("Pending Co-Ownership Requests:");
        for (CoOwnershipRequest request : requests) {
            System.out.println(request);
        }

        System.out.print("Enter request ID to approve: ");
        int requestId = scanner.nextInt();
        scanner.nextLine();

        try {
            appController.approveCoOwnership(requestId);
            System.out.println("Request approved successfully.");
        } catch (RuntimeException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays the logged-in user's profile information.
     */
    private void viewMyProfile() {
        if (loggedInUser != null) {
            System.out.println("User Profile: " + loggedInUser);
        } else {
            System.out.println("No user is logged in.");
        }
    }

    /**
     * Displays all accounts associated with the logged-in customer.
     */
    private void myAccounts() {
        if (loggedInUser instanceof Customer) {
            Customer customer = (Customer) loggedInUser;
            List<Account> accounts = new ArrayList<>();

            try {
                  accounts = appController.getAccountsForCustomer(customer.getId());

            } catch (IOException e){
                System.out.println("Error getting accounts for customer: " + customer.getId());
            }

            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
            } else {
                System.out.println("Your Accounts:");
                for (Account account : accounts) {
                    System.out.println(account);
                }
            }
        } else {
            System.out.println("No customer is logged in.");
        }
    }

    /**
     * Prompts the customer to open a new account and handles the creation process.
     */
    private void openNewAccount() {
        if (loggedInUser instanceof Customer) {
            Customer customer = (Customer) loggedInUser;

            System.out.println("Choose Account type:");
            System.out.println("1. Checking Account (0.5% transaction fee)  2. Savings Account (4.5%)");
            System.out.print("Option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            Account newAccount = null;

            if (option != 1 && option != 2) {
                System.out.println("Invalid option.");
                return;
            }

            if (option == 1) {
                System.out.println("New Checking Account");
                System.out.print("Enter initial deposit: ");
                double initialDeposit = scanner.nextDouble();
                scanner.nextLine();

                try {
                    newAccount = appController.createCheckingAccount(customer.getId(), initialDeposit);
                    System.out.println("New account opened: " + newAccount);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            } else if (option == 2) {
                System.out.println("New Savings Account");
                System.out.print("Enter initial deposit: ");
                double initialDeposit = scanner.nextDouble();
                scanner.nextLine();

                try {
                    newAccount = appController.createSavingsAccount(customer.getId(), initialDeposit);
                    System.out.println("New account opened: " + newAccount);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            System.out.println("No customer is logged in.");
        }
    }

    /**
     * Allows the customer to close one of their accounts after confirmation.
     */
    private void closeAccount() {
        System.out.print("Enter Account ID to close: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Are you sure you want to close this account? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            try {
                appController.closeAccount(loggedInUser.getId(), accountId);
                System.out.println("Account closed successfully.");
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Account closure cancelled.");
        }
    }

    /**
     * Displays actions available to a logged-in employee and handles user choices.
     */
    private void employeeActions() {
        while (true) {
            System.out.println("\n--- Employee Actions ---");
            System.out.println("1. Add Customer");
            System.out.println("2. Read User");
            System.out.println("3. Update Customer");
            System.out.println("4. Delete User");
            System.out.println("5. List all Users");
            System.out.println("6. List all Accounts");
            System.out.println("7. View all Transactions");
            System.out.println("8. View Account Logs for Account");
            System.out.println("9. View Bank Info");
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 0) {
                System.out.println("Logging out...");
                loggedInUser = null; // Clear logged in user
                break;
            }

            switch (option) {
                case 1:
                    createUser();
                    break;
                case 2:
                    readUser();
                    break;
                case 3:
                    updateUser();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    listAllUsers();
                    break;
                case 6:
                    listAllAccounts();
                    break; // TODO case 7, 8, 9
                case 7:
                    listAllTransactions();
                    break;
                case 8:
                    viewLogsSpecific();
                    break;
                case 9:
                    viewBankInfo();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /**
     * Lists Bank Info
     */
    private void viewBankInfo() {
        try {
            Bank bank = Bank.getInstance("Bank Info Germana", appController.getAllAccounts(), appController.getAllCustomers(), appController.getAllEmployees(), appController.getAllLoans());
            System.out.println(bank.getName());
            System.out.println();
            System.out.println("Accounts (" + bank.getAccounts().size() + ")");
            System.out.println("-------------------");
            System.out.println("Customers (" + bank.getCustomers().size() + ")");
            System.out.println("-------------------");
            System.out.println("Employees (" + bank.getEmployees().size() + ")");
            System.out.println("-------------------");
            System.out.println("Loans (" + bank.getEmployees().size() + ")");
        } catch (RuntimeException | IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Lists all Transactions
     */
    private void listAllTransactions(){
        List<Transaction> transactions = new ArrayList<>();
        try {
            transactions = appController.getAllTransactions();
        } catch (RuntimeException e){
            System.out.println(e);
        }

        transactions.forEach(System.out::println);
    }

    /**
     * Lists all Accounts
     */
    private void listAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try {
            accounts = appController.getAllAccounts();
        } catch (IOException e){
            System.out.println("Unable to read account list.");
        }

        accounts.forEach(System.out::println);
    }

    /**
     * Allows an employee to create a new customer account.
     */
    private void createUser() {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        try {
            int generatedId = appController.createCustomer(firstName, lastName, email, phoneNumber, password);
            System.out.println("Customer created with ID: " + generatedId);
        } catch (RuntimeException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Reads and displays information about a user based on the provided ID.
     */
    private void readUser() {
        System.out.print("Enter ID to read: ");
        int readId = scanner.nextInt();
        scanner.nextLine();

        try {
            User user = appController.readUser(readId);
            if (user != null) {
                System.out.println("User found: " + user);
            } else {
                System.out.println("User not found.");
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates an existing user's information based on the provided details.
     */
    private void updateUser() {
        System.out.print("Enter ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter First Name: ");
        String userFirstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String userLastName = scanner.nextLine();

        System.out.print("Enter Email: ");
        String userEmail = scanner.nextLine();

        System.out.print("Enter Phone Number: ");
        String userPhone = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        try {
            appController.updateUser(userId, userFirstName, userLastName, userEmail, userPhone, password);
            System.out.println("User updated successfully.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes a user based on the provided ID.
     */
    private void deleteUser() {
        System.out.print("Enter ID to delete: ");
        int deleteId = scanner.nextInt();
        scanner.nextLine();

        try {
            appController.deleteUser(deleteId);
            System.out.println("User deleted successfully.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Lists all users in the system.
     */
    private void listAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            users = appController.getAllUsers();
        } catch (IOException e){
            System.out.println("Unable to read user list.");
        }

        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * Allows the customer to select a checking account and perform loan-and transaction-related actions.
     */
    private void setSelectedAccount() {
        System.out.print("Select a Checking Account: ");

        List<Account> checkingAccounts = new ArrayList<>();

        try {
            checkingAccounts = appController.getAccountsForCustomer(loggedInUser.getId())
                    .stream()
                    .filter(account -> account instanceof CheckingAccount)
                    .toList();
        } catch (IOException e){
            System.out.println("Error getting accounts.");
        }

        if(checkingAccounts.isEmpty()) {
            System.out.println("No checking accounts found. Open one first in order to get a loan or make a transaction.");
            return;
        }

        System.out.println();
        for (Account account : checkingAccounts) {
            System.out.println(account);
        }

        System.out.print("Account ID: ");
        int accountId = scanner.nextInt();

        this.selectedAccount = checkingAccounts.stream()
                .filter(account -> account.getId() == accountId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Displays loan-related actions available to the customer and handles user choices.
     */
    private void loanActions() throws IOException {
        while (true) {
            System.out.println("\n--- Loan Actions ---");
            System.out.println("1. Apply for Loan");
            System.out.println("2. Pay Loan");
            System.out.println("3. View Ongoing Loans");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 0) {
                break;
            }

            switch (option) {
                case 1:
                    applyForLoan();
                    break;
                case 2:
                    payLoan();
                    break;
                case 3:
                    listAllLoans();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /**
     * Prompts the customer to apply for a loan and handles the loan creation process.
     */
    private void applyForLoan() {
        System.out.print("Enter loan amount: ");
        double loanAmount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter term in months: (6 min/ 120 max): ");
        int termMonths = scanner.nextInt();
        scanner.nextLine();

        try {
            this.appController.getLoan((Customer) loggedInUser, selectedAccount, loanAmount, termMonths);
            System.out.println("Loan taken successfully!");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }


    private void viewLogsSpecific() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter the Account ID: ");
            int accountId = scanner.nextInt();
            scanner.nextLine();

            Account account = this.appController.getAccountById(accountId);

            if (account == null) {
                System.out.println("Error: Account with ID " + accountId + " does not exist.");
                return;
            }

            List<String> logs = this.appController.getLogsForAccount((CheckingAccount) account);

            if (logs.isEmpty()) {
                System.out.println("No logs available for Account ID: " + accountId);
            } else {
                System.out.println("Logs for Account ID: " + accountId);
                AtomicInteger index = new AtomicInteger();
                logs.stream()
                        .distinct()
                        .forEach(log -> System.out.println(index.incrementAndGet() + ". " + log));
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred: Can not display for SavingsAccounts, only CheckingAccounts.");
        }
    }


    /**
     * Lists all ongoing loans associated with the logged-in customer.
     */
    private void listAllLoans() {
        int count = 0;
        for (Loan loan : this.appController.viewLoansStatus((Customer) loggedInUser)) {
            System.out.println(loan);
            count++;
        }

        if (count == 0) {
            System.out.println("No loans found.");
        }
    }

    /**
     * Allows the customer to pay off an existing loan.
     */
    private void payLoan() throws IOException {
        System.out.print("Enter loan ID: ");
        int loanId = scanner.nextInt();
        scanner.nextLine();

        Loan loan = null;
        try {
            loan = this.appController.getLoanById(loanId, (Customer) loggedInUser);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        if (loan != null) {
            System.out.println("Loan Information:");
            System.out.println(" - Remaining amount: " + loan.getLoanAmount());
            System.out.println(" - Remaining term: " + loan.getTermMonths() + " months");

            System.out.print("Enter payment amount: ");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine();

            this.appController.payLoan(loanId, (Customer) loggedInUser, selectedAccount, paymentAmount);
        } else {
            System.out.println("Loan not found.");
        }
    }

    private void transactionActions() {
        while (true) {
            System.out.println("\n--- Transaction Actions ---");
            System.out.println("1. Make a transaction");
            System.out.println("2. View previous transactions");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 0) {
                break;
            }

            switch (option) {
                case 1:
                    makeTransaction();
                    break;
                case 2:
                    displayTransactions();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /**
     * Displays transactions made from the selected account.
     */
    private void displayTransactions() {
        List<Transaction> transactions = appController.getTransactionsForAccount((CheckingAccount) selectedAccount);
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("Transactions:");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    /**
     * Prompts the customer to make a transaction and handles the transaction process.
     */
    private void makeTransaction() {
        System.out.println("Available Accounts: ");
        List<Account> accounts = new ArrayList<>();
        try {
            accounts = appController.getAllUsers().stream().filter(user -> user != loggedInUser)
                    .flatMap(user -> {
                        try {
                            return appController.getAccountsForCustomer(user.getId()).stream();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            System.out.println("Error reading available accounts.");
        }

        for(Account account : accounts) {
            System.out.println(account);
        }

        System.out.print("Enter destination account ID: ");
        int destinationAccountId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        CheckingAccount destinationAccount = (CheckingAccount) accounts.stream()
                .filter(account -> account.getId() == destinationAccountId)
                .findFirst()
                .orElse(null);

        try {
            appController.makeTransaction((CheckingAccount) selectedAccount, destinationAccount, amount);
            System.out.println("Transaction successful.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays the logs for the selected account.
     */
    private void viewLogs() {
        try {
            int index = 0;
            List<String> logs = this.appController.getLogsForAccount((CheckingAccount) selectedAccount);

            for (int i = 0; i < logs.size(); i += 2) {
                System.out.println(++index + ". " + logs.get(i));
            }
        } catch (RuntimeException e) {
            System.out.println("Error: No logs available");
        }
    }

    /**
     * Displays actions available to view sorted data.
     */
    private void viewSortedData() {
        while (true) {
            System.out.println("\n--- Sorted Data ---");
            System.out.println("1. View Accounts Sorted by Balance");
            System.out.println("2. Sort by Creation Date (Newest First)");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 0) {
                break;
            }

            switch (option) {
                case 1:
                    viewAccountsSortedByBalance();
                    break;
                case 2:
                    viewAccountsSortedByCreationDate();
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /**
     * Retrieves and displays accounts sorted by balance.
     */
    private void viewAccountsSortedByBalance() {
        try {
            List<Account> sortedAccounts = appController.getAccountsSortedByBalance(loggedInUser.getId());
            System.out.println("\n--- Accounts Sorted by Balance ---");
            for (Account account : sortedAccounts) {
                System.out.println(account);
            }
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Populate for in memory
     */
    private void populateInMemoryData() {
        try {
            int customer1Id = appController.createCustomer("Darius", "Bolos", "darius@gmail.com", "1234567890", "1234");
            int customer2Id = appController.createCustomer("Rares", "Popa", "rares@gmail.com", "0987654321", "1234");
            int customer3Id = appController.createCustomer("Jane", "Johnson", "jane@gmail.com", "1122334455", "1234");

            appController.createCheckingAccount(customer1Id, 1000.0);
            appController.createCheckingAccount(customer2Id, 500.0);
            appController.createCheckingAccount(customer3Id, 750.0);

        } catch (RuntimeException | IOException e) {
            System.out.println("Error populating in-memory data: " + e.getMessage());
        }
    }

    /**
     * Retrieves and displays accounts sorted by balance.
     */
    private void viewAccountsSortedByCreationDate(){
        List<Account> sortedAccounts = appController.getAccountsSortedByCreationDate(loggedInUser.getId());
        System.out.println("\n--- Accounts Sorted by Balance ---");
        for (Account account : sortedAccounts) {
            System.out.println(account);
        }
    }

    /**
     * Retrieves and displays transactions sorted by date.
     */
    private void viewTransactionsSortedByDate() {
        List<Transaction> sortedTransactions = appController.getTransactionsSortedByDate((CheckingAccount)selectedAccount);
        System.out.println("\n--- Transactions Sorted by Date ---");
        for (Transaction transaction : sortedTransactions) {
            System.out.println(transaction);
        }
    }

    /**
     * Retrieves and displays loans sorted by amount.
     */
    private void viewLoansSortedByAmount() {
        List<Loan> sortedLoans = appController.getLoansSortedByAmount((Customer)loggedInUser);
        System.out.println("\n--- Loans Sorted by Amount ---");
        for (Loan loan : sortedLoans) {
            System.out.println(loan);
        }
    }

    /**
     * Retrieves and displays accounts with balance above a specified threshold.
     */
    private void viewAccountsAboveThreshold() {
        System.out.print("Enter threshold amount: ");
        double threshold = scanner.nextDouble();
        scanner.nextLine();

        try {
            List<Account> accounts = appController.getAccountsWithBalanceAbove(loggedInUser.getId(), threshold);

            for (Account account : accounts) {
                System.out.println(account);
            }
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves and displays transactions above a specified threshold.
     */
    private void viewTransactionsAboveThreshold() {
        setSelectedAccount();
        System.out.print("Enter threshold amount: ");
        double threshold = scanner.nextDouble();
        scanner.nextLine();

        try {
            List<Transaction> transactions = appController.getTransactionsAboveAmount((CheckingAccount) selectedAccount, threshold);

            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    private void viewFilteredData() {
        while (true) {
            System.out.println("\n--- Filtered Data ---");
            System.out.println("1. View Accounts with Balance Above Threshold");
            System.out.println("2. View Transactions Above Threshold");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 0) {
                break;
            }

            switch (option) {
                case 1:
                    viewAccountsAboveThreshold();
                    break;
                case 2:
                    viewTransactionsAboveThreshold();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
