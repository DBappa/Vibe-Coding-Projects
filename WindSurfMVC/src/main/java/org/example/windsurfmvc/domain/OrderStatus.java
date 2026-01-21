package org.example.windsurfmvc.domain;

/**
 * Represents the status of a customer order.
 * The status transitions should follow this order:
 * NEW → VALIDATED → IN_PROGRESS → PICKED_UP → COMPLETED
 * 
 * An order can be CANCELLED from any status except COMPLETED.
 */
public enum OrderStatus {
    /**
     * Order has been created but not yet validated
     */
    NEW,
    
    /**
     * Order has been validated and is ready for processing
     */
    VALIDATED,
    
    /**
     * Order is being processed
     */
    IN_PROGRESS,
    
    /**
     * Order has been picked up by the customer
     */
    PICKED_UP,
    
    /**
     * Order has been completed successfully
     */
    COMPLETED,
    
    /**
     * Order has been cancelled
     */
    CANCELLED;
    
    /**
     * Checks if a status transition is valid
     * @param newStatus The status to transition to
     * @return true if the transition is valid, false otherwise
     */
    public boolean canTransitionTo(OrderStatus newStatus) {
        if (this == CANCELLED || this == COMPLETED) {
            return false; // Terminal states
        }
        
        if (newStatus == CANCELLED) {
            return true; // Can cancel from any non-terminal state
        }
        
        // Normal flow: NEW → VALIDATED → IN_PROGRESS → PICKED_UP → COMPLETED
        return switch (this) {
            case NEW -> newStatus == VALIDATED;
            case VALIDATED -> newStatus == IN_PROGRESS;
            case IN_PROGRESS -> newStatus == PICKED_UP;
            case PICKED_UP -> newStatus == COMPLETED;
            default -> false;
        };
    }
}
