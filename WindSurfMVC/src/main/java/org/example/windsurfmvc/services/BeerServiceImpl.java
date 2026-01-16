package org.example.windsurfmvc.services;

import lombok.RequiredArgsConstructor;
import org.example.windsurfmvc.entities.Beer;
import org.example.windsurfmvc.repositories.BeerRepository;
import org.example.windsurfmvc.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Beer getBeerById(Integer id) {
        return beerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beer not found with id: " + id));
    }

    @Override
    @Transactional
    public Beer saveBeer(Beer beer) {
        return beerRepository.save(beer);
    }

    @Override
    @Transactional
    public Beer updateBeer(Integer id, Beer beerDetails) {
        Beer existingBeer = beerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beer not found with id: " + id));
        
        existingBeer.setBeerName(beerDetails.getBeerName());
        existingBeer.setBeerStyle(beerDetails.getBeerStyle());
        existingBeer.setUpc(beerDetails.getUpc());
        existingBeer.setPrice(beerDetails.getPrice());
        existingBeer.setQuantityOnHand(beerDetails.getQuantityOnHand());
        
        return beerRepository.save(existingBeer);
    }

    @Override
    @Transactional
    public void deleteBeer(Integer id) {
        beerRepository.deleteById(id);
    }
}
