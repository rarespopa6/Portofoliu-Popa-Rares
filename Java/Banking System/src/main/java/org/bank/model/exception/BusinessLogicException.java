package org.bank.model.exception;

/**
 * Exception to indicate errors related to business logic.
 * This exception is typically used when an operation violates the rules
 * or constraints defined by the application's domain logic.
 */
public class BusinessLogicException extends RuntimeException {

    /**
     * Constructs a new BusinessLogicException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception.
     */
    public BusinessLogicException(String message) {
        super(message);
    }
}
