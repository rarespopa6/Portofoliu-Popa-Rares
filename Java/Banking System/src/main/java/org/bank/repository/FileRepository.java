package org.bank.repository;

import org.bank.model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing persistence of objects to and from CSV files.
 * Supports operations such as create, read, update, delete, and find all entries.
 *
 * @param <T> Type of objects managed by the repository, which must implement {@link Identifiable}.
 */
public class FileRepository<T extends Identifiable> implements IRepository<T> {
    private final String filePath;
    private final String userAccountPath = "data/user_accounts.csv";

    /**
     * Constructs a new repository with a specified file path.
     *
     * @param filePath The path to the CSV file where data will be stored.
     */
    public FileRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Creates a new object in the repository, assigning it a new ID.
     *
     * @param obj The object to create and store.
     * @return The generated ID for the new object.
     */
    @Override
    public int create(T obj) {
        int newId = generateId();
        setId(obj, newId);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(toCsvFormat(obj) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (obj instanceof Account) {
            saveUserAccountRelationships((Account) obj);
        }
        return newId;
    }

    /**
     * Reads an object by ID from the repository.
     *
     * @param id The ID of the object to retrieve.
     * @return The object with the specified ID, or null if not found.
     */
    @Override
    public T read(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T obj = fromCsvFormat(line);
                if (getId(obj) == id) {
                    return obj;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates an existing object in the repository.
     *
     * @param obj The updated object to save.
     */
    @Override
    public void update(T obj) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T currentObj = fromCsvFormat(line);
                if (getId(currentObj) == getId(obj)) {
                    line = toCsvFormat(obj);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (obj instanceof Account) {
            updateUserAccountRelationships((Account) obj);
        }
    }

    /**
     * Deletes an object by ID from the repository.
     *
     * @param id The ID of the object to delete.
     * @throws IOException If an I/O error occurs during deletion.
     */
    @Override
    public void delete(int id) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T currentObj = fromCsvFormat(line);
                if (getId(currentObj) != id) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        }

        if (filePath.contains("accounts.csv")) {
            deleteUserAccountRelationships(id);
        }
    }

    /**
     * Retrieves all objects from the repository.
     *
     * @return A list of all objects in the repository.
     */
    public List<T> findAll() {
        List<T> allObjects = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T obj = fromCsvFormat(line);
                allObjects.add(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allObjects;
    }

    /**
     * Converts an object to its CSV string representation.
     *
     * @param obj The object to convert.
     * @return A CSV-formatted string representing the object.
     * @throws IllegalArgumentException If the object's type is not supported.
     */
    private String toCsvFormat(T obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return user.getId() + "," + user.getFirstName() + "," + user.getLastName() + "," +
                    user.getEmail() + "," + user.getPhoneNumber() + "," + user.getPassword();
        } else if (obj instanceof Account) {
            Account account = (Account) obj;
            String type = (account instanceof CheckingAccount) ? "CheckingAccount" :
                    (account instanceof SavingsAccount) ? "SavingsAccount" : "Unknown";
            return account.getId() + "," + account.getBalance() + "," + account.getCreationTime() + "," + type;
        }
        throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getSimpleName());
    }

    /**
     * Parses a CSV line to create an object of type T.
     *
     * @param csvLine The CSV line to parse.
     * @return The created object.
     * @throws IllegalArgumentException If the line format is unsupported.
     */
    private T fromCsvFormat(String csvLine) {
        String[] parts = csvLine.split(",");
        if (filePath.contains("users.csv")) {
            int id = Integer.parseInt(parts[0]);
            String firstName = parts[1];
            String lastName = parts[2];
            String email = parts[3];
            String phoneNumber = parts[4];
            String password = parts[5];
            return (T) new Customer(id, firstName, lastName, email, phoneNumber, password);
        } else if (filePath.contains("accounts.csv")) {
            int id = Integer.parseInt(parts[0]);
            double balance = Double.parseDouble(parts[1]);
            LocalDateTime creationTime = LocalDateTime.parse(parts[2]);
            String accountType = parts[3];

            List<User> customers = loadCustomersForAccount(id, new FileRepository<>("data/users.csv"));
            if (accountType.equals("CheckingAccount")) {
                CheckingAccount newAccount = new CheckingAccount(customers, balance, 0.5);
                newAccount.setId(id);
                newAccount.setCreationTime(creationTime);
                return (T) newAccount;
        } else if (accountType.equals("SavingsAccount")) {
                SavingsAccount newAccount =  new SavingsAccount(customers, balance, 4.5);
                newAccount.setId(id);
                newAccount.setCreationTime(creationTime);
                return (T) newAccount;
            } else {
                throw new IllegalArgumentException("Unknown account type: " + accountType);
            }
        }
        throw new IllegalArgumentException("Unsupported file format for line: " + csvLine);
    }

    /**
     * Loads a list of customers associated with a specific account ID.
     *
     * @param accountId The ID of the account.
     * @param userRepository The user repository to retrieve customer information.
     * @return A list of users associated with the account.
     */
    private List<User> loadCustomersForAccount(int accountId, FileRepository<User> userRepository) {
        List<User> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(userAccountPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int accId = Integer.parseInt(parts[0]);
                int userId = Integer.parseInt(parts[1]);
                if (accId == accountId) {
                    User user = userRepository.read(userId);
                    if (user != null) customers.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * Saves relationships between users and an account to the CSV file.
     *
     * @param account The account to save relationships for.
     */
    private void saveUserAccountRelationships(Account account) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userAccountPath, true))) {
            for (User user : account.getCustomers()) {
                BufferedReader reader = new BufferedReader(new FileReader(userAccountPath));
                boolean found = false;
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (Integer.parseInt(parts[0]) == account.getId() && Integer.parseInt(parts[1]) == user.getId()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    writer.write(account.getId() + "," + user.getId() + "\n");
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates relationships between users and an account by first deleting and then saving.
     *
     * @param account The account to update relationships for.
     */
    private void updateUserAccountRelationships(Account account) {
        deleteUserAccountRelationships(account.getId());
        saveUserAccountRelationships(account);
    }

    /**
     * Deletes relationships associated with a specific account ID.
     *
     * @param accountId The ID of the account for which relationships will be deleted.
     */
    private void deleteUserAccountRelationships(int accountId) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(userAccountPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int accId = Integer.parseInt(parts[0]);
                if (accId != accountId) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userAccountPath))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the ID of an object.
     *
     * @param obj The object from which to retrieve the ID.
     * @return The ID of the object.
     * @throws IllegalArgumentException If the object's type is not supported.
     */
    private int getId(T obj) {
        if (obj instanceof User) {
            return ((User) obj).getId();
        } else if (obj instanceof Account) {
            return ((Account) obj).getId();
        }
        throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getSimpleName());
    }

    /**
     * Sets the ID of an object.
     *
     * @param obj The object to set the ID for.
     * @param id The ID to assign to the object.
     * @throws IllegalArgumentException If the object's type is not supported.
     */
    private void setId(T obj, int id) {
        if (obj instanceof User) {
            ((User) obj).setId(id);
        } else if (obj instanceof Account) {
            ((Account) obj).setId(id);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getSimpleName());
        }
    }

    /**
     * Generates a new unique ID based on the existing IDs in the repository.
     *
     * @return A new unique ID.
     */
    private int generateId() {
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T obj = fromCsvFormat(line);
                maxId = Math.max(maxId, getId(obj));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }
}
