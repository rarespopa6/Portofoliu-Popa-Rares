package org.bank.model.exception;

/**
 * Exception to indicate that data validation has failed.
 * This exception is commonly used when validating user input or business rules
 * and the input does not meet the required criteria.
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the validation failure.
     */
    public ValidationException(String message) {
        super(message);
    }
}
