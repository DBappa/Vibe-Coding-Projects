package org.example.windsurfmvc.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.windsurfmvc.dtos.BeerDto;
import org.example.windsurfmvc.dtos.ErrorResponse;
import org.example.windsurfmvc.exceptions.ResourceNotFoundException;
import org.example.windsurfmvc.services.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing beers
 */
@Slf4j
@RestController
@RequestMapping(
    path = "/api/v1/beers",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class BeerController {

    private final BeerService beerService;

    /**
     * Get all beers
     *
     * @return list of all beers
     */
    @GetMapping
    public ResponseEntity<List<BeerDto>> getAllBeers() {
        log.debug("Received request to get all beers");
        return ResponseEntity.ok(beerService.getAllBeers());
    }

    /**
     * Get a beer by ID
     *
     * @param id the ID of the beer to retrieve
     * @return the beer with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable UUID id) {
        log.debug("Received request to get beer with id: {}", id);
        return ResponseEntity.ok(beerService.getBeerById(id));
    }

    /**
     * Create a new beer
     *
     * @param beerDto the beer data to create
     * @return the created beer
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BeerDto> createBeer(@Valid @RequestBody BeerDto beerDto) {
        log.debug("Received request to create beer: {}", beerDto.getBeerName());
        return new ResponseEntity<>(
            beerService.saveBeer(beerDto),
            HttpStatus.CREATED
        );
    }

    /**
     * Update an existing beer
     *
     * @param id the ID of the beer to update
     * @param beerDto the updated beer data
     * @return the updated beer
     */
    @PutMapping(
        path = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BeerDto> updateBeer(
            @PathVariable UUID id,
            @Valid @RequestBody BeerDto beerDto) {
        log.debug("Received request to update beer with id: {}", id);
        return ResponseEntity.ok(beerService.updateBeer(id, beerDto));
    }

    /**
     * Delete a beer by ID
     *
     * @param id the ID of the beer to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable UUID id) {
        log.debug("Received request to delete beer with id: {}", id);
        beerService.deleteBeer(id);
    }
    
    /**
     * Handle ResourceNotFoundException
     *
     * @param ex the exception to handle
     * @return error response with NOT_FOUND status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Not Found", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
