package org.example.windsurfmvc.services;

import org.example.windsurfmvc.dtos.BeerDto;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing beers
 */
public interface BeerService {
    
    /**
     * Get all beers
     *
     * @return list of all beers as DTOs
     */
    List<BeerDto> getAllBeers();

    /**
     * Get a beer by its ID
     *
     * @param id the ID of the beer to retrieve
     * @return the beer DTO if found
     */
    BeerDto getBeerById(UUID id);

    /**
     * Save a new beer
     *
     * @param beerDto the beer data to save
     * @return the saved beer as a DTO
     */
    BeerDto saveBeer(BeerDto beerDto);

    /**
     * Update an existing beer
     *
     * @param id the ID of the beer to update
     * @param beerDto the updated beer data
     * @return the updated beer as a DTO
     */
    BeerDto updateBeer(UUID id, BeerDto beerDto);

    /**
     * Delete a beer by its ID
     *
     * @param id the ID of the beer to delete
     */
    void deleteBeer(UUID id);
}
