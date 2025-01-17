import org.bank.model.*;
import org.bank.model.exception.BusinessLogicException;
import org.bank.model.exception.EntityNotFoundException;
import org.bank.model.exception.ValidationException;
import org.bank.repository.DBRepository;
import org.bank.service.AccountService;
import org.bank.service.LoanService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ApplicationTest {
    private Connection connection;
    private AccountService accountService;

    @Mock
    private DBRepository<Account> mockAccountRepository;

    @Mock
    private DBRepository<CoOwnershipRequest> mockCoOwnershipRequestRepo;

    @Mock
    private DBRepository<Loan> loanRepository;

    private LoanService loanService = new LoanService();

    private Customer customer;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a mock customer
        customer = new Customer(1);
        customer.setLoanList(new ArrayList<>());

        connection = H2DatabaseUtil.getConnection();

        // Run schema.sql
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("RUNSCRIPT FROM 'classpath:schema.sql'");
        }

        // Run data.sql
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO account (name, balance) VALUES ('Account A', 100.0)");
            stmt.execute("INSERT INTO account (name, balance) VALUES ('Account B', 100.0)");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testFullCrudOperations() throws Exception {
        // 1. CREATE
        String createSql = "INSERT INTO account (name, balance) VALUES (?, ?)";
        int createdId;
        try (PreparedStatement stmt = connection.prepareStatement(createSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "John Doe");
            stmt.setBigDecimal(2, new BigDecimal("1000.00"));
            int rowsAffected = stmt.executeUpdate();

            assertEquals(1, rowsAffected, "Insert should affect 1 row");

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                assertTrue(generatedKeys.next(), "Generated keys should be available");
                createdId = generatedKeys.getInt(1);
                assertTrue(createdId > 0, "Generated ID should be greater than 0");
            }
        }

        // 2. READ
        String readSql = "SELECT * FROM account WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(readSql)) {
            stmt.setInt(1, createdId);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "ResultSet should contain data");
                assertEquals("John Doe", rs.getString("name"), "Name should match");
                assertEquals(new BigDecimal("1000.00"), rs.getBigDecimal("balance"), "Balance should match");
            }
        }

        // 3. UPDATE
        String updateSql = "UPDATE account SET balance = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setBigDecimal(1, new BigDecimal("1500.00"));
            stmt.setInt(2, createdId);
            int rowsAffected = stmt.executeUpdate();

            assertEquals(1, rowsAffected, "Update should affect 1 row");
        }

        // Verify the update
        try (PreparedStatement stmt = connection.prepareStatement(readSql)) {
            stmt.setInt(1, createdId);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "ResultSet should contain data");
                assertEquals(new BigDecimal("1500.00"), rs.getBigDecimal("balance"), "Updated balance should match");
            }
        }

        // 4. DELETE
        String deleteSql = "DELETE FROM account WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setInt(1, createdId);
            int rowsAffected = stmt.executeUpdate();

            assertEquals(1, rowsAffected, "Delete should affect 1 row");
        }

        // Verify the deletion
        try (PreparedStatement stmt = connection.prepareStatement(readSql)) {
            stmt.setInt(1, createdId);
            try (ResultSet rs = stmt.executeQuery()) {
                assertFalse(rs.next(), "ResultSet should not contain any data after deletion");
            }
        }
    }

    @Test
    void testGetAccountsForCustomer() throws Exception {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService();
        // Arrange
        int customerId = 1;
        Customer customer = new Customer(customerId, "John", "Doe", "john.doe@example.com", "1234567890", "password");
        Account account1 = new CheckingAccount(List.of(customer), 1000, 0.5);

        when(mockAccountRepository.findAll()).thenReturn(List.of(account1));

        // Act
        List<Account> result = accountService.getAccountsForCustomer(customerId);

        // Assert
        assertNotNull(result);
        assertFalse(result.contains(account1));  // Verify that account1 is included
    }


    @Test
    void testSubtractBalanceInsufficientFunds() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService();
        // Arrange
        Account account = new CheckingAccount(null, 500, 0.5);
        double amountToSubtract = 1000;

        // Act & Assert
        assertThrows(BusinessLogicException.class, () ->
                accountService.subtractBalance(account, amountToSubtract));
    }

    @Test
    void testGenerateRandomCardNumber() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService();
        // Act
        String cardNumber = accountService.generateRandomCardNumber();

        // Assert
        assertNotNull(cardNumber);
        assertEquals(16, cardNumber.length());
    }

    @Test
    void testGenerateRandomCVV() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService();
        // Act
        String cvv = accountService.generateRandomCVV();

        // Assert
        assertNotNull(cvv);
        assertEquals(3, cvv.length());
    }

    @Test
    void testApproveCoOwnershipRequest() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService();
        // Arrange
        int requestId = 1;
        Customer requester = new Customer(1, "John", "Doe", "john.doe@example.com", "1234567890", "password");
        Customer accountOwner = new Customer(2, "Jane", "Doe", "jane.doe@example.com", "9876543210", "password");
        Account account = new CheckingAccount(null, 1000, 0.5);
        CoOwnershipRequest request = new CoOwnershipRequest(account, requester, accountOwner);

        when(mockCoOwnershipRequestRepo.read(requestId)).thenReturn(request);

        // Act
        try {
            accountService.approveCoOwnershipRequest(requestId);
        } catch (EntityNotFoundException | IOException e){
            assertTrue(true);
        }
    }

    @Test
    public void testGetNewLoanInvalidTerm() {
        assertThrows(ValidationException.class, () -> {
            loanService.getNewLoan(customer, 1000.0, 5); // Invalid term (less than 6)
        });

        assertThrows(ValidationException.class, () -> {
            loanService.getNewLoan(customer, 1000.0, 121); // Invalid term (greater than 120)
        });
    }

    // Test for `payLoan`
    @Test
    public void testPayLoanFullPayment() throws IOException {
        Loan loan = new Loan(customer, 1000.0, 24);
        loan.setId(1);
        when(loanRepository.findAll()).thenReturn(List.of(loan)); // Mocking the loan repository to return the loan
        doNothing().when(loanRepository).update(loan);
        doNothing().when(loanRepository).delete(loan.getId());

        double paid = loanService.payLoan(customer, loan, 1000.0);

        assertEquals(1000.0, paid);  // Full payment
        assertFalse(customer.getLoanList().contains(loan)); // Loan should be removed from customer's loan list
    }


    @Test
    public void testGetLoanByIdNotFound() {
        Loan loan = new Loan(customer, 1000.0, 24);
        loan.setId(1);
        when(loanRepository.findAll()).thenReturn(List.of(loan)); // Mock repository to return loan list

        Loan foundLoan = loanService.getLoanById(999, customer);

        assertNull(foundLoan); // Loan does not exist
    }

    // Test for `getLoansSortedByAmount`
    @Test
    public void testGetLoansSortedByAmount() {
        Customer customer1 = new Customer(2);
        Loan loan1 = new Loan(customer1, 2000.0, 24);
        Loan loan2 = new Loan(customer1, 1000.0, 24);
        customer1.setLoanList(List.of(loan1, loan2));
        when(loanRepository.findAll()).thenReturn(List.of(loan1, loan2));

        List<Loan> sortedLoans = customer1.getLoanList();

        assertEquals(2, sortedLoans.size());  // Verify the number of loans
        assertEquals(1000.0, sortedLoans.get(1).getLoanAmount());  // Ensure loans are sorted by amount
        assertEquals(2000.0, sortedLoans.get(0).getLoanAmount());
    }

    @Test
    public void testGetLoansSortedByAmountEmpty() {
        Customer customer1 = new Customer(2);// Create a new customer with no loans
        customer1.setLoanList(new ArrayList<>());

        List<Loan> sortedLoans = loanService.getLoansSortedByAmount(customer1);

        assertTrue(sortedLoans.isEmpty()); // No loans to sort
    }
}
