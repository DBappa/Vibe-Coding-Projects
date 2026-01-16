package org.example.windsurfmvc.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.windsurfmvc.entities.Beer;
import org.example.windsurfmvc.services.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beers")
@RequiredArgsConstructor
public class BeerController {

    private final BeerService beerService;

    @GetMapping
    public ResponseEntity<List<Beer>> getAllBeers() {
        return ResponseEntity.ok(beerService.getAllBeers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beer> getBeerById(@PathVariable Integer id) {
        return ResponseEntity.ok(beerService.getBeerById(id));
    }

    @PostMapping
    public ResponseEntity<Beer> createBeer(@Valid @RequestBody Beer beer) {
        return new ResponseEntity<>(beerService.saveBeer(beer), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable Integer id) {
        beerService.deleteBeer(id);
    }
}
