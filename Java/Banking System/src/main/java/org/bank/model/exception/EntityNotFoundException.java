package org.bank.model.exception;

/**
 * Exception to indicate that a specific entity was not found in the system.
 * This exception is commonly used when querying the database or other data sources
 * and the requested entity does not exist.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructs a new EntityNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
