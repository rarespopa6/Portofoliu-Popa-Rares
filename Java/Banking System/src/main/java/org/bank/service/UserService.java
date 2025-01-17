package org.bank.service;

import org.bank.config.DBConfig;
import org.bank.model.Account;
import org.bank.model.Customer;
import org.bank.model.Employee;
import org.bank.model.User;
import org.bank.model.exception.BusinessLogicException;
import org.bank.model.exception.DatabaseException;
import org.bank.model.exception.EntityNotFoundException;
import org.bank.repository.DBRepository;
import org.bank.repository.FileRepository;
import org.bank.repository.IRepository;
import org.bank.repository.InMemoryRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class that provides functionality for managing users, including customers and employees.
 * This service handles user creation, retrieval, updating, and deletion, as well as account management for customers.
 */
public class UserService {
    private IRepository<User> userRepository; // = new DBRepository<>(User.class, DBConfig.USERS_TABLE);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(String storageMethod) {
        switch (storageMethod.toLowerCase()) {
            case "inmemory":
                userRepository = new InMemoryRepository<>();
                break;
            case "file":
                userRepository = new FileRepository<>("data/users.csv");
                break;
            case "db":
                userRepository = new DBRepository<>(User.class, DBConfig.USERS_TABLE);
                break;
            default:
                userRepository = new DBRepository<>(User.class, DBConfig.USERS_TABLE);
                break;
        }
    }

    /**
     * Creates a new employee and saves it in the repository. Ensures the email is unique.
     *
     * @param id the unique identifier for the employee.
     * @param firstName the first name of the employee.
     * @param lastName the last name of the employee.
     * @param email the email address of the employee.
     * @param phoneNumber the phone number of the employee.
     * @param password the plain-text password for the employee, which will be hashed.
     * @param salary the salary of the employee.
     * @param role the role of the employee.
     * @throws RuntimeException if an employee with the specified email already exists.
     */
    public void createEmployee(int id, String firstName, String lastName, String email, String phoneNumber, String password, int salary, String role) throws IOException {
        if (userExistsByEmail(email)) {
            throw new BusinessLogicException("An employee with this email already exists");
        }
        String hashedPassword = passwordEncoder.encode(password);
        User employee = new Employee(id, firstName, lastName, email, phoneNumber, hashedPassword, salary, role);

        userRepository.create(employee);
    }

    /**
     * Creates a new customer and saves it in the repository. Ensures the email is unique.
     *
     * @param firstName the first name of the customer.
     * @param lastName the last name of the customer.
     * @param email the email address of the customer.
     * @param phoneNumber the phone number of the customer.
     * @param password the plain-text password for the customer, which will be hashed.
     * @return the unique ID assigned to the newly created customer.
     * @throws RuntimeException if a customer with the specified email already exists.
     */
    public int createCustomer(String firstName, String lastName, String email, String phoneNumber, String password) throws IOException {
        if (userExistsByEmail(email)) {
            throw new BusinessLogicException("A customer with this email already exists");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new BusinessLogicException("Invalid email address");
        }

        String hashedPassword = passwordEncoder.encode(password);
        Customer customer = new Customer(firstName, lastName, email, phoneNumber, hashedPassword);

        return userRepository.create(customer);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the unique identifier of the user.
     * @return the user with the specified ID.
     * @throws RuntimeException if the user is not found.
     */
    public User readUser(int id) {
        User user = userRepository.read(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    /**
     * Updates an existing user's information in the repository.
     *
     * @param id the unique identifier of the user to update.
     * @param firstName the new first name of the user.
     * @param lastName the new last name of the user.
     * @param email the new email address of the user.
     * @param phoneNumber the new phone number of the user.
     * @param password the new plain-text password for the user, which will be hashed.
     * @throws RuntimeException if the user is not found.
     */
    public void updateUser(int id, String firstName, String lastName, String email, String phoneNumber, String password) {
        if (!userExists(id)) {
            throw new EntityNotFoundException("User not found for update");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User updatedUser = new Customer(id, firstName, lastName, email, phoneNumber, hashedPassword);
        userRepository.update(updatedUser);
    }

    /**
     * Deletes a user from the repository by their ID.
     *
     * @param id the unique identifier of the user to delete.
     * @throws RuntimeException if the user is not found.
     */
    public void deleteUser(int id) {
        if (!userExists(id)) {
            throw new EntityNotFoundException("User not found for deletion");
        }
        try {
            userRepository.delete(id);
        } catch (Exception e){throw new DatabaseException("Can not delete user");}
    }

    /**
     * Retrieves a list of all users currently stored in the repository.
     *
     * @return a list of all users.
     */
    public List<User> getAllUsers() throws IOException {
        return userRepository.findAll();
    }

    /**
     * Retrieves a list of all customers currently stored in the repository.
     *
     * @return a list of all customers.
     */
    public List<Customer> getAllCustomers() throws IOException {
        List<User> allUsers = userRepository.findAll();
        List<Customer> customers = new ArrayList<>();

        for (User user : allUsers) {
            if (user instanceof Customer) {
                customers.add((Customer) user);
            }
        }

        return customers;
    }

    /**
     * Retrieves a list of all employees currently stored in the repository.
     *
     * @return a list of all employees.
     */
    public List<Employee> getAllEmployees() throws IOException {
        List<User> allUsers = userRepository.findAll();
        List<Employee> employees = new ArrayList<>();

        for (User user : allUsers) {
            if (user instanceof Employee) {
                employees.add((Employee) user);
            }
        }

        return employees;
    }


    /**
     * Adds a bank account to a customer based on their ID.
     *
     * @param customerId the unique identifier of the customer.
     * @param account the account to be added to the customer.
     * @throws RuntimeException if the customer is not found or the user is not of type Customer.
     */
    public void addAccountToCustomer(int customerId, Account account) {
        User user = userRepository.read(customerId);

        if (user instanceof Customer customer) {
            customer.addAccount(account);
        } else {
            throw new EntityNotFoundException("Customer not found or not a valid customer type.");
        }
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to find.
     * @return the user with the specified email, or {@code null} if not found.
     */
    public User getUserByEmail(String email) throws IOException {
        return userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if a user exists based on their email address.
     *
     * @param email the email address to check.
     * @return {@code true} if a user with the specified email exists, {@code false} otherwise.
     */
    private boolean userExistsByEmail(String email) throws IOException {
        return userRepository.findAll().stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }

    /**
     * Checks if a user exists in the repository by their ID.
     *
     * @param id the unique identifier of the user.
     * @return {@code true} if a user with the specified ID exists, {@code false} otherwise.
     */
    private boolean userExists(int id) {
        return userRepository.read(id) != null;
    }

    /**
     * Retrieves a list of users sorted by name.
     *
     * @return A list of users sorted alphabetically by first and last name.
     */
    public List<User> getUsersSortedByName() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getFirstName)
                        .thenComparing(User::getLastName))
                .collect(Collectors.toList());
    }
}
