package org.example.windsurfmvc.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.windsurfmvc.dtos.BeerDto;
import org.example.windsurfmvc.entities.Beer;
import org.example.windsurfmvc.exceptions.ResourceNotFoundException;
import org.example.windsurfmvc.mappers.BeerMapper;
import org.example.windsurfmvc.repositories.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BeerDto> getAllBeers() {
        log.debug("Fetching all beers");
        return beerRepository.findAll().stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BeerDto getBeerById(UUID id) {
        log.debug("Fetching beer with id: {}", id);
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto)
                .orElseThrow(() -> new ResourceNotFoundException("Beer not found with id: " + id));
    }

    @Override
    @Transactional
    public BeerDto saveBeer(BeerDto beerDto) {
        log.debug("Saving new beer: {}", beerDto.getBeerName());
        Beer beer = beerMapper.beerDtoToBeer(beerDto);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    @Transactional
    public BeerDto updateBeer(UUID id, BeerDto beerDto) {
        log.debug("Updating beer with id: {}", id);
        Beer existingBeer = beerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beer not found with id: " + id));
        
        // Update the existing beer with the new values from DTO
        beerMapper.updateBeerFromDto(beerDto, existingBeer);
        
        Beer updatedBeer = beerRepository.save(existingBeer);
        return beerMapper.beerToBeerDto(updatedBeer);
    }

    @Override
    @Transactional
    public void deleteBeer(UUID id) {
        log.debug("Deleting beer with id: {}", id);
        Beer beer = beerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beer not found with id: " + id));
        beerRepository.delete(beer);
        log.debug("Deleted beer with id: {}", id);
    }
}
