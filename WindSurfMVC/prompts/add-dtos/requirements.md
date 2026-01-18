# DTO Implementation Requirements

## Objective
Enhance the application by implementing Data Transfer Objects (DTOs) to decouple the API layer from the persistence layer, improving maintainability and security.

## Requirements

### 1. Create BeerDto
- Create a new POJO called `BeerDto` in the `dtos` package
- Include all properties from the `Beer` entity
- Use Lombok annotations (`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`)
- Add validation annotations where appropriate

### 2. Create MapStruct Mapper
- Create a mapper interface in the `mappers` package
- Implement mapping between `Beer` entity and `BeerDto`
- Ignore ID, createdDate, and updateDate when mapping from DTO to entity
- Configure MapStruct to work with Spring's dependency injection

### 3. Update Service Layer
- Modify `BeerService` interface to use `BeerDto`
- Update `BeerServiceImpl` to handle DTO conversions
- Ensure all service methods work with DTOs instead of entities

### 4. Update Controller
- Modify `BeerController` to accept and return `BeerDto` objects
- Add proper validation annotations to controller methods
- Update API documentation if present

### 5. Update Tests
- Update unit and integration tests to work with DTOs
- Add validation tests for DTOs
- Ensure all existing tests pass with the new DTO structure

## Technical Details
- Use MapStruct for object mapping
- Use Jakarta Validation for input validation
- Maintain backward compatibility with existing API consumers
- Follow RESTful best practices

## Dependencies
- MapStruct
- Lombok
- Spring Validation
- MapStruct Spring Extensions
