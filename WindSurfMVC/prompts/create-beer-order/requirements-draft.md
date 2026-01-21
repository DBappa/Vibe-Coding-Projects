# Beer Order System - JPA Implementation Requirements

## Overview
This document outlines the JPA implementation requirements for the Beer Order System based on the provided ERD. The system will manage beer products, customers, and their orders.

## Entities and Relationships

### 1. Beer (Existing)
- **Purpose**: Represents a beer product in the system
- **Table**: `beers`
- **Fields**:
  - `id` (UUID, PK)
  - `version` (Integer, for optimistic locking)
  - `beerName` (String, not null)
  - `beerStyle` (String, not null)
  - `upc` (String, unique)
  - `price` (BigDecimal)
  - `quantityOnHand` (Integer)
  - `createdDate` (LocalDateTime)
  - `updateDate` (LocalDateTime)

### 2. Customer
- **Purpose**: Represents a customer who can place orders
- **Table**: `customers`
- **Fields**:
  - `id` (UUID, PK)
  - `version` (Integer)
  - `name` (String, not null)
  - `email` (String, unique, not null)
  - `createdDate` (LocalDateTime)
  - `lastModifiedDate` (LocalDateTime)

### 3. CustomerOrder (Order is a reserved keyword in SQL)
- **Purpose**: Represents a customer's order
- **Table**: `customer_orders`
- **Fields**:
  - `id` (UUID, PK)
  - `version` (Integer)
  - `orderNumber` (String, unique, not null)
  - `orderDate` (LocalDateTime, not null)
  - `orderStatus` (Enum: NEW, IN_PROGRESS, COMPLETED, CANCELLED)
  - `customerId` (UUID, FK to Customer, not null)
  - `createdDate` (LocalDateTime)
  - `updateDate` (LocalDateTime)

### 4. OrderLine
- **Purpose**: Represents a line item in an order
- **Table**: `order_lines`
- **Fields**:
  - `id` (UUID, PK)
  - `version` (Integer)
  - `orderId` (UUID, FK to CustomerOrder, not null)
  - `beerId` (UUID, FK to Beer, not null)
  - `quantity` (Integer, not null, min=1)
  - `price` (BigDecimal, not null)
  - `createdDate` (LocalDateTime)
  - `updateDate` (LocalDateTime)

## Entity Relationships

1. **Customer to CustomerOrder (1:N)**
   - One Customer can have many CustomerOrders
   - CustomerOrder owns the relationship
   - Cascade: PERSIST, MERGE
   - Fetch: Lazy

2. **CustomerOrder to OrderLine (1:N)**
   - One CustomerOrder can have multiple OrderLines
   - CustomerOrder owns the relationship
   - Cascade: ALL (if order is deleted, all its lines should be deleted)
   - Fetch: Lazy
   - Orphan removal: true (if an order line is removed from the collection, it should be deleted)

3. **Beer to OrderLine (1:N)**
   - One Beer can be in many OrderLines
   - OrderLine owns the relationship
   - Cascade: None (we don't want to cascade operations from Beer to OrderLine)
   - Fetch: EAGER (since we'll likely always need the beer details when viewing an order line)

## Implementation Details

### Base Entity
Consider creating a `BaseEntity` class with common fields:
- id
- version
- createdDate
- updateDate

### Enums
1. `OrderStatus` - Status of an order (NEW, IN_PROGRESS, COMPLETED, CANCELLED)

### Lombok Annotations
- Use `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Use `@Builder` for entity creation
- Use `@ToString.Exclude` on bidirectional relationships to prevent circular references
- Use `@EqualsAndHashCode` with `callSuper = true` if extending `BaseEntity`

### Validation Annotations
- Use Jakarta Validation annotations for field validation
- Add appropriate validation messages
- Consider creating custom validators for complex validation rules

### Auditing
- Use Spring Data JPA auditing (`@CreatedDate`, `@LastModifiedDate`)
- Consider implementing `AuditorAware` for tracking who made changes

## Sample Code Snippets

### BaseEntity
```java
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    
    @Version
    private Integer version;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
    
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
}
```

### OrderStatus Enum
```java
public enum OrderStatus {
    NEW, IN_PROGRESS, COMPLETED, CANCELLED
}
```

### CustomerOrder Entity
```java
@Entity
@Table(name = "customer_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOrder extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String orderNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.NEW;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<OrderLine> orderLines = new HashSet<>();
    
    // Helper method for bidirectional relationship
    public void addOrderLine(OrderLine orderLine) {
        if (orderLines == null) {
            orderLines = new HashSet<>();
        }
        orderLines.add(orderLine);
        orderLine.setOrder(this);
    }
}
```

### OrderLine Entity
```java
@Entity
@Table(name = "order_lines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLine extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private CustomerOrder order;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beer_id", nullable = false)
    private Beer beer;
    
    @Column(nullable = false)
    @Min(1)
    private Integer quantity;
    
    @Column(nullable = false)
    @Positive
    private BigDecimal price;
}
```

## Next Steps
1. Review and finalize the requirements
2. Create detailed implementation plan
3. Set up database migrations (if using Flyway/Liquibase)
4. Implement entities, repositories, services, and controllers
5. Add validation and error handling
6. Write unit and integration tests
