package org.example.windsurfmvc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Exception thrown when there is insufficient stock to fulfill an order.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class InsufficientStockException extends RuntimeException {
    
    private final UUID beerId;
    private final int requested;
    private final int available;

    public InsufficientStockException(UUID beerId, int requested, int available) {
        super(String.format("Insufficient stock for beer %s. Requested: %d, Available: %d", 
            beerId, requested, available));
        this.beerId = beerId;
        this.requested = requested;
        this.available = available;
    }

    public UUID getBeerId() {
        return beerId;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}
