package org.example.windsurfmvc.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Beer entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BeerDto {
    
    private UUID id;
    
    @NotBlank(message = "Beer name is required")
    @Size(min = 2, max = 50, message = "Beer name must be between 2 and 50 characters")
    private String beerName;
    
    @NotBlank(message = "Beer style is required")
    private String beerStyle;
    
    @NotBlank(message = "UPC is required")
    @Size(min = 6, max = 20, message = "UPC must be between 6 and 20 characters")
    private String upc;
    
    @NotNull(message = "Quantity on hand is required")
    @PositiveOrZero(message = "Quantity on hand must be 0 or greater")
    private Integer quantityOnHand;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    @Digits(integer = 5, fraction = 2, message = "Price must have up to 5 integer and 2 fraction digits")
    private BigDecimal price;
    
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private Integer version;
}
