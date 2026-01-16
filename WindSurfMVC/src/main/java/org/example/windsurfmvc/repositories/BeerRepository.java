package org.example.windsurfmvc.repositories;

import org.example.windsurfmvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, Integer> {
}
