# Beer Order System - Final Requirements

## Table of Contents
1. [Overview](#overview)
2. [Entity Specifications](#entity-specifications)
3. [Relationship Mappings](#relationship-mappings)
4. [Database Schema](#database-schema)
5. [Implementation Guidelines](#implementation-guidelines)
6. [Validation Rules](#validation-rules)
7. [Auditing and Logging](#auditing-and-logging)
8. [Exception Handling](#exception-handling)
9. [Testing Strategy](#testing-strategy)

## Overview
This document specifies the implementation requirements for the Beer Order System, which extends the existing beer catalog with order management capabilities. The system will be built using Spring Boot, JPA/Hibernate, and follows the existing project structure and patterns.

## Entity Specifications

### 1. Beer (Existing)
- **Package**: `org.example.windsurfmvc.entities`
- **Table**: `beers`
- **Fields**:
  - `id` (UUID, PK, auto-generated)
  - `version` (Integer, for optimistic locking)
  - `beerName` (String, not null, max 50 chars)
  - `beerStyle` (String, not null, max 50 chars)
  - `upc` (String, unique, not null, max 20 chars)
  - `price` (BigDecimal, not null, precision 5, scale 2)
  - `quantityOnHand` (Integer, not null, min 0)
  - `createdDate` (LocalDateTime, auto-set on creation)
  - `updateDate` (LocalDateTime, auto-updated on modification)

### 2. Customer
- **Package**: `org.example.windsurfmvc.entities`
- **Table**: `customers`
- **Fields**:
  - `id` (UUID, PK, auto-generated)
  - `version` (Integer, for optimistic locking)
  - `name` (String, not null, max 100 chars)
  - `email` (String, unique, not null, valid email format)
  - `createdDate` (LocalDateTime, auto-set)
  - `lastModifiedDate` (LocalDateTime, auto-updated)

### 3. CustomerOrder
- **Package**: `org.example.windsurfmvc.entities`
- **Table**: `customer_orders`
- **Fields**:
  - `id` (UUID, PK, auto-generated)
  - `version` (Integer, for optimistic locking)
  - `orderNumber` (String, unique, not null, format: ORD-{UUID})
  - `orderStatus` (Enum: NEW, VALIDATED, IN_PROGRESS, PICKED_UP, COMPLETED, CANCELLED)
  - `customer` (ManyToOne relationship to Customer, not null)
  - `orderLines` (OneToMany relationship to OrderLine, cascade ALL, orphan removal)
  - `createdDate` (LocalDateTime, auto-set)
  - `updateDate` (LocalDateTime, auto-updated)

### 4. OrderLine
- **Package**: `org.example.windsurfmvc.entities`
- **Table**: `order_lines`
- **Fields**:
  - `id` (UUID, PK, auto-generated)
  - `version` (Integer, for optimistic locking)
  - `order` (ManyToOne to CustomerOrder, not null)
  - `beer` (ManyToOne to Beer, not null, eager fetch)
  - `quantity` (Integer, not null, min 1)
  - `price` (BigDecimal, not null, matches beer price at time of order)
  - `createdDate` (LocalDateTime, auto-set)
  - `updateDate` (LocalDateTime, auto-updated)

## Relationship Mappings

### Customer -> CustomerOrder (1:N)
- **Owner Side**: CustomerOrder (owns the foreign key)
- **Mapped By**: `customer` field in CustomerOrder
- **Cascade**: PERSIST, MERGE
- **Fetch**: LAZY
- **Validation**: Customer must exist before creating order

### CustomerOrder -> OrderLine (1:N)
- **Owner Side**: OrderLine (owns the foreign key)
- **Mapped By**: `order` field in OrderLine
- **Cascade**: ALL (cascade all operations to order lines)
- **Orphan Removal**: true
- **Fetch**: LAZY
- **Validation**: Order must have at least one order line

### Beer -> OrderLine (1:N)
- **Owner Side**: OrderLine (owns the foreign key)
- **Mapped By**: `beer` field in OrderLine
- **Cascade**: NONE
- **Fetch**: EAGER (always load beer details with order line)
- **Validation**: Beer must exist and have sufficient quantity

## Database Schema
```sql
CREATE TABLE customers (
    id UUID PRIMARY KEY,
    version INTEGER,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP
);

CREATE TABLE customer_orders (
    id UUID PRIMARY KEY,
    version INTEGER,
    order_number VARCHAR(36) NOT NULL UNIQUE,
    order_status VARCHAR(20) NOT NULL,
    customer_id UUID NOT NULL,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE order_lines (
    id UUID PRIMARY KEY,
    version INTEGER,
    order_id UUID NOT NULL,
    beer_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES customer_orders(id),
    FOREIGN KEY (beer_id) REFERENCES beers(id)
);
```

## Implementation Guidelines

### Base Entity
Create a `BaseEntity` class in `org.example.windsurfmvc.entities` to hold common fields and annotations:

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

### Enums
Create `OrderStatus` enum in `org.example.windsurfmvc.domain`:

```java
public enum OrderStatus {
    NEW, VALIDATED, IN_PROGRESS, PICKED_UP, COMPLETED, CANCELLED
}
```

### DTOs
Create corresponding DTOs in `org.example.windsurfmvc.dtos`:
- `CustomerDto`
- `CustomerOrderDto`
- `OrderLineDto`
- `BeerOrderPagedList` (for pagination)

### Repositories
Create repositories in `org.example.windsurfmvc.repositories`:
- `CustomerRepository`
- `CustomerOrderRepository`
- `OrderLineRepository`

### Services
Create services in `org.example.windsurfmvc.services`:
- `CustomerService`
- `CustomerOrderService`

### Controllers
Create REST controllers in `org.example.windsurfmvc.controllers`:
- `CustomerController`
- `CustomerOrderController`

## Validation Rules

### Customer
- Name: Required, 2-100 characters
- Email: Required, valid format, unique

### CustomerOrder
- Order Number: Required, unique, format ORD-{UUID}
- Customer: Required, must exist
- Status: Required, valid enum value
- Order Lines: At least one required

### OrderLine
- Beer: Required, must exist, must have sufficient quantity
- Quantity: Required, minimum 1
- Price: Required, must match beer price at time of order

## Auditing and Logging
- Use Spring Data JPA Auditing
- Add `@EnableJpaAuditing` to main application
- Implement `AuditorAware<UUID>` for user tracking
- Add comprehensive logging at service layer
- Log all order state changes

## Exception Handling
- Create custom exceptions in `org.example.windsurfmvc.exceptions`:
  - `ResourceNotFoundException`
  - `OrderValidationException`
  - `InsufficientStockException`
- Implement `@ControllerAdvice` for global exception handling
- Return appropriate HTTP status codes and error messages

## Testing Strategy

### Unit Tests
- Test entity validation
- Test service layer business logic
- Test repository queries

### Integration Tests
- Test controller endpoints
- Test database operations
- Test transaction boundaries

### Test Data
- Use `@DataJpaTest` for repository tests
- Use `@WebMvcTest` for controller tests
- Use `@SpringBootTest` for full integration tests
- Use TestContainers for database testing

## API Endpoints

### Customer Endpoints
- `GET /api/v1/customers` - List all customers (paginated)
- `GET /api/v1/customers/{customerId}` - Get customer by ID
- `POST /api/v1/customers` - Create new customer
- `PUT /api/v1/customers/{customerId}` - Update customer
- `DELETE /api/v1/customers/{customerId}` - Delete customer

### Order Endpoints
- `GET /api/v1/customers/{customerId}/orders` - List customer's orders
- `GET /api/v1/orders` - List all orders (paginated)
- `GET /api/v1/orders/{orderId}` - Get order by ID
- `POST /api/v1/customers/{customerId}/orders` - Create new order
- `PUT /api/v1/orders/{orderId}` - Update order
- `PATCH /api/v1/orders/{orderId}/status` - Update order status
- `DELETE /api/v1/orders/{orderId}` - Cancel order

## Security Considerations
- Add Spring Security
- Implement JWT authentication
- Secure endpoints based on roles
- Validate ownership of resources
- Prevent SQL injection
- Implement rate limiting

## Performance Considerations
- Add appropriate database indexes
- Use pagination for list endpoints
- Implement caching where appropriate
- Use DTO projections for read operations
- Consider read replicas for reporting

## Monitoring and Metrics
- Add Actuator endpoints
- Configure health checks
- Add custom metrics for order processing
- Set up alerts for failed orders
- Monitor database performance
