package org.bank.repository;

import org.bank.config.DBConfig;
import org.bank.model.*;
import org.bank.model.exception.DatabaseException;
import org.bank.model.exception.EntityNotFoundException;
import org.bank.model.mapper.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The DBRepository class provides a generic implementation of the IRepository interface.
 * It handles CRUD operations for database entities.
 *
 * @param <T> The type of entity being handled (e.g., Account, User, CoOwnershipRequest).
 */
public class DBRepository<T extends Identifiable> implements IRepository<T> {
    private final String tableName;
    private final Class<T> type;
    private final Map<Class<?>, Mapper<?>> mappers = new HashMap<>();
    private final String dbUrl = DBConfig.DB_URL;
    private final String dbUser = DBConfig.DB_USER;
    private final String dbPassword = DBConfig.DB_PASSWORD;
    /**
     * Constructs a DBRepository for the specified entity type and table.
     *
     * @param type The class type of the entity (e.g., Account.class).
     * @param tableName The name of the corresponding table in the database.
     */
    public DBRepository(Class<T> type, String tableName) {
        this.type = type;
        this.tableName = tableName;

        // Account
        registerMapper(Account.class, new AccountMapper());
        registerMapper(CheckingAccount.class, new AccountMapper());
        registerMapper(SavingsAccount.class, new AccountMapper());

        // User
        registerMapper(User.class, new UserMapper());
        registerMapper(Customer.class, new UserMapper());
        registerMapper(Employee.class, new UserMapper());

        // CoOwnershipRequest
        registerMapper(CoOwnershipRequest.class, new CoOwnershipRequestMapper());

        // Transaction
        registerMapper(Transaction.class, new TransactionMapper());

        // Loan
        registerMapper(Loan.class, new LoanMapper());

        // AccountLogs
        registerMapper(AccountLogs.class, new AccountLogsMapper());
    }

    /**
     * Registers a mapper for the specified class type.
     *
     * @param clazz The class type (e.g., Account.class).
     * @param mapper The corresponding Mapper instance.
     */
    public void registerMapper(Class<?> clazz, Mapper<?> mapper) {
        mappers.put(clazz, mapper);
    }

    /**
     * Retrieves the appropriate mapper for the entity type.
     *
     * @return The Mapper for the current entity type.
     * @throws IllegalArgumentException if no mapper is found for the current entity type.
     */
    @SuppressWarnings("unchecked")
    private Mapper<T> getMapper() {
        Mapper<?> mapper = mappers.get(type);

        if (mapper == null) {
            throw new DatabaseException("No mapper found for type: " + type.getName());
        }

        return (Mapper<T>) mapper;
    }

    /**
     * Establishes a connection to the database.
     *
     * @return The database connection.
     * @throws SQLException If the connection attempt fails.
     */
    private Connection getConnection() throws DatabaseException {
        try {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            throw new DatabaseException("Can not connect to database.");
        }
    }

    /**
     * Creates a new entity in the database.
     *
     * @param obj The entity to create (e.g., Account, User).
     * @return The ID of the newly created entity.
     * @throws IllegalArgumentException If the provided object is null.
     */
    @Override
    public int create(T obj) {
        if (obj == null) throw new EntityNotFoundException("Object to create cannot be null");

        String sql = buildInsertSql(obj);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            populateInsertStatement(stmt, obj);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    obj.setId(generatedId);

                    if (obj instanceof Account) {
                        Account account = (Account) obj;
                        populateAccountUserRelationship(conn, account);
                    }

                    return generatedId;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error creating new entity.");
        }
        return 0;
    }

    /**
     * Reads an entity from the database by its ID.
     *
     * @param id The ID of the entity to read.
     * @return The entity corresponding to the specified ID, or null if not found.
     */
    @Override
    public T read(int id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    T entity = getMapper().map(rs);
                    populateRelationships(entity, conn);
                    return entity;
                }
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException("Can not read entity.");
        }
        return null;
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param obj The entity to update.
     * @throws IllegalArgumentException If the provided object is null.
     */
    @Override
    public void update(T obj) {
        if (obj == null) throw new IllegalArgumentException("Object to update cannot be null");

        String sql = buildUpdateSql(obj);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            populateUpdateStatement(stmt, obj);
            stmt.executeUpdate();

            if (obj instanceof Account) {
                Account account = (Account) obj;
                populateAccountUserRelationship(conn, account);
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException("Can not update entity.");
        }
    }

    /**
     * Deletes an entity from the database by its ID.
     *
     * @param id The ID of the entity to delete.
     */
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new EntityNotFoundException("Can not delete entity.");
        }
    }

    /**
     * Retrieves all entities from the table.
     *
     * @return A list of all entities of the specified type.
     */
    @Override
    public List<T> findAll() {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                T entity = getMapper().map(rs);
                populateRelationships(entity, conn);
                results.add(entity);
            }
        } catch (SQLException e) {
            throw new EntityNotFoundException("Can not find all entities.");
        }

        return results;
    }

    /**
     * Populates relationships for the given entity from the database.
     *
     * @param entity The entity whose relationships should be populated.
     * @param conn The database connection.
     * @throws SQLException If an SQL error occurs.
     */
    private void populateRelationships(T entity, Connection conn) throws SQLException {
        if (entity instanceof Account) {
            Account account = (Account) entity;
            String sql = "SELECT u.* FROM users u " +
                    "JOIN accountuser au ON u.id = au.user_id " +
                    "WHERE au.account_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, account.getId());
                try (ResultSet rs = stmt.executeQuery()) {
                    List<User> users = new ArrayList<>();
                    while (rs.next()) {
                        Customer customer = new Customer(
                                rs.getInt("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("phone_number"),
                                rs.getString("password")
                        );
                        users.add(customer);
                    }
                    account.setOwner(users);
                }
            }
        }
    }

    /**
     * Builds the SQL query for inserting an entity into the database.
     *
     * @param obj The entity to insert.
     * @return The SQL query string for inserting the entity.
     */
    private String buildInsertSql(T obj) {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName).append(" (");

        List<String> fields = new ArrayList<>();
        if (obj instanceof Account) {
            fields.addAll(Arrays.asList("balance", "creation_time", "type"));
            if (obj instanceof CheckingAccount) {
                fields.add("transaction_fee");
            } else if (obj instanceof SavingsAccount) {
                fields.add("interest_rate");
            }
        } else if (obj instanceof User) {
            fields.addAll(Arrays.asList("first_name", "last_name", "email", "phone_number", "password", "type"));
            if (obj instanceof Customer) {

            } else if (obj instanceof Employee) {
                fields.addAll(Arrays.asList("salary", "role"));
            }
        }  else if (obj instanceof CoOwnershipRequest) {
            fields.addAll(Arrays.asList("account_id", "requester_id", "owner_id", "approved"));
        } else if (obj instanceof Transaction) {
            fields.addAll(Arrays.asList("source_account_id", "destination_account_id", "amount", "transaction_date"));
        } else if (obj instanceof Loan) {
            fields.addAll(Arrays.asList("borrower_id", "loan_amount", "term_months"));
        } else if (obj instanceof AccountLogs) {
            fields.addAll(Arrays.asList("account_id", "message"));
        }

        sql.append(String.join(", ", fields)).append(") VALUES (");
        sql.append(String.join(", ", Collections.nCopies(fields.size(), "?"))).append(")");
        return sql.toString();
    }

    /**
     * Populates the prepared statement with the values of the entity for insertion.
     *
     * @param stmt The prepared statement to populate.
     * @param obj The entity whose data should be inserted.
     * @throws SQLException If an SQL error occurs.
     */
    private void populateInsertStatement(PreparedStatement stmt, T obj) throws SQLException {
        int index = 1;

        if (obj instanceof Account) {
            Account account = (Account) obj;
            stmt.setDouble(index++, account.getBalance());
            stmt.setTimestamp(index++, Timestamp.valueOf(account.getCreationTime()));

            if (obj instanceof CheckingAccount) {
                stmt.setString(index++, "CHECKING");
                stmt.setDouble(index, ((CheckingAccount) account).getTransactionFee());
            } else if (obj instanceof SavingsAccount) {
                stmt.setString(index++, "SAVINGS");
                stmt.setDouble(index, ((SavingsAccount) account).getInterestRate());
            }
        } else if (obj instanceof User) {
            User user = (User) obj;
            stmt.setString(index++, user.getFirstName());
            stmt.setString(index++, user.getLastName());
            stmt.setString(index++, user.getEmail());
            stmt.setString(index++, user.getPhoneNumber());
            stmt.setString(index++, user.getPassword());

            if (obj instanceof Customer) {
                stmt.setString(index, "CUSTOMER");
            } else if (obj instanceof Employee) {
                stmt.setString(index++, "EMPLOYEE");
                Employee employee = (Employee) obj;
                stmt.setInt(index++, employee.getSalary());
                stmt.setString(index, employee.getRole());
            }
        } else if (obj instanceof CoOwnershipRequest) {
            CoOwnershipRequest request = (CoOwnershipRequest) obj;

            stmt.setInt(index++, request.getAccount().getId()); // account_id
            stmt.setInt(index++, request.getRequester().getId()); // requester_id
            stmt.setInt(index++, request.getAccountOwner().getId()); // owner_id
            stmt.setBoolean(index, request.isApproved()); // approved (true/false)
        } else if (obj instanceof Transaction) {
            Transaction transaction = (Transaction) obj;

            stmt.setInt(index++, transaction.getSourceAccount().getId()); // source_account_id
            stmt.setInt(index++, transaction.getDestinationAccount().getId()); // destination_account_id
            stmt.setDouble(index++, transaction.getAmount()); // amount
            java.sql.Date sqlDate = new java.sql.Date(transaction.getDate().getTime());
            stmt.setDate(index, sqlDate); // transaction_date
        } else if (obj instanceof Loan) {
            Loan loan = (Loan) obj;

            stmt.setInt(index++, loan.getBorrower().getId()); // borrower_id
            stmt.setDouble(index++, loan.getLoanAmount()); // loan_amount
            stmt.setInt(index, loan.getTermMonths()); // term_months
        } else if (obj instanceof AccountLogs) {
            AccountLogs logs = (AccountLogs) obj;

            if (!logs.getLogs().isEmpty()) {
                stmt.setInt(1, logs.getAccount().getId());
                stmt.setString(2, logs.getLogs().get(0));
            }
        }

    }

    /**
     * Builds the SQL query for updating an entity in the database.
     *
     * @param obj The entity to update.
     * @return The SQL query string for updating the entity.
     */
    private String buildUpdateSql(T obj) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");

        List<String> fields = new ArrayList<>();
        if (obj instanceof Account) {
            fields.addAll(Arrays.asList("balance = ?", "creation_time = ?"));
            if (obj instanceof CheckingAccount) {
                fields.add("transaction_fee = ?");
            } else if (obj instanceof SavingsAccount) {
                fields.add("interest_rate = ?");
            }
        } else if (obj instanceof User) {
            fields.addAll(Arrays.asList("first_name = ?", "last_name = ?", "email = ?", "phone_number = ?", "password = ?"));
            if (obj instanceof Employee) {
                fields.addAll(Arrays.asList("salary = ?", "role = ?"));
            }
        } else if (obj instanceof Loan) {
            fields.add("loan_amount = ?");
        }

        sql.append(String.join(", ", fields)).append(" WHERE id = ?");
        return sql.toString();
    }

    /**
     * Populates the prepared statement with the values of the entity for updating.
     *
     * @param stmt The prepared statement to populate.
     * @param obj The entity whose data should be updated.
     * @throws SQLException If an SQL error occurs.
     */
    private void populateUpdateStatement(PreparedStatement stmt, T obj) throws SQLException {
        int index = 1;

        if (obj instanceof Account) {
            Account account = (Account) obj;
            stmt.setDouble(index++, account.getBalance());

            if (account.getCreationTime() == null) {
                account.setCreationTime(LocalDateTime.now());
            }

            stmt.setTimestamp(index++, Timestamp.valueOf(account.getCreationTime()));

            if (obj instanceof CheckingAccount) {
                stmt.setDouble(index++, ((CheckingAccount) account).getTransactionFee());
            } else if (obj instanceof SavingsAccount) {
                stmt.setDouble(index++, ((SavingsAccount) account).getInterestRate());
            }
        } else if (obj instanceof User) {
            User user = (User) obj;
            stmt.setString(index++, user.getFirstName());
            stmt.setString(index++, user.getLastName());
            stmt.setString(index++, user.getEmail());
            stmt.setString(index++, user.getPhoneNumber());
            stmt.setString(index++, user.getPassword());

            if (obj instanceof Employee) {
                Employee employee = (Employee) obj;
                stmt.setInt(index++, employee.getSalary());
                stmt.setString(index++, employee.getRole());
            }
        } else if (obj instanceof Loan) {
            Loan loan = (Loan) obj;
            stmt.setDouble(index++, loan.getLoanAmount());
        }

        stmt.setInt(index, obj.getId());
    }

    /**
     * Populates the relationship between an account and its users (owners).
     *
     * @param conn The database connection.
     * @param account The account whose user relationship should be populated.
     * @throws SQLException If an SQL error occurs.
     */
    private void populateAccountUserRelationship(Connection conn, Account account) throws SQLException {
        String sqlCheck = "SELECT COUNT(*) FROM accountuser WHERE account_id = ? AND user_id = ?";
        String sqlInsert = "INSERT INTO accountuser (account_id, user_id) VALUES (?, ?)";

        try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
            for (User owner : account.getCustomers()) {
                checkStmt.setInt(1, account.getId());
                checkStmt.setInt(2, owner.getId());

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                            insertStmt.setInt(1, account.getId());
                            insertStmt.setInt(2, owner.getId());
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }
        }
    }
}
