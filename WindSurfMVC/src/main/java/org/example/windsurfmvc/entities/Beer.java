package org.example.windsurfmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity representing a Beer
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "beers")
public class Beer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Version
    private Integer version;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String beerName;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String beerStyle;

    @NotBlank
    @Size(max = 20)
    @Column(unique = true, nullable = false, updatable = false)
    private String upc;

    @Min(0)
    @Column(nullable = false)
    private Integer quantityOnHand;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateDate;
}
