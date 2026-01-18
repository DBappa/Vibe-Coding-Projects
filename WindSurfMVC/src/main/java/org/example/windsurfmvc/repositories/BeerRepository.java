package org.example.windsurfmvc.repositories;

import org.example.windsurfmvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA Repository for Beer entities
 */
public interface BeerRepository extends JpaRepository<Beer, UUID> {
    
    /**
     * Find a beer by its UPC (Universal Product Code)
     *
     * @param upc the UPC to search for
     * @return an Optional containing the found beer, or empty if not found
     */
    Optional<Beer> findByUpc(String upc);
}
