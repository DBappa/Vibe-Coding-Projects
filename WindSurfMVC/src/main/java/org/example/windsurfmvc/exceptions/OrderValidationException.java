package org.example.windsurfmvc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when order validation fails.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderValidationException extends RuntimeException {
    public OrderValidationException(String message) {
        super(message);
    }

    public OrderValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
