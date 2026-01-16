package org.example.windsurfmvc.services;

import org.example.windsurfmvc.entities.Beer;

import java.util.List;

public interface BeerService {
    List<Beer> getAllBeers();
    Beer getBeerById(Integer id);
    Beer saveBeer(Beer beer);
    Beer updateBeer(Integer id, Beer beer);
    void deleteBeer(Integer id);
}
