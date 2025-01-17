package org.bank.model.exception;

/**
 * Exception to indicate errors related to database operations.
 * This exception is typically used to wrap SQL exceptions or handle
 * database-specific errors in a user-friendly manner.
 */
public class DatabaseException extends RuntimeException {

    /**
     * Constructs a new DatabaseException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception.
     */
    public DatabaseException(String message) {
        super(message);
    }
}
