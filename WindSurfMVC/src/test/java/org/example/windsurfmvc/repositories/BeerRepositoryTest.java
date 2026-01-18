package org.example.windsurfmvc.repositories;

import org.example.windsurfmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("1234567890")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build());

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testFindById() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("Find Me")
                .beerStyle("Lager")
                .upc("0987654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build());

        Optional<Beer> foundBeer = beerRepository.findById(savedBeer.getId());
        assertThat(foundBeer).isPresent();
        assertThat(foundBeer.get().getBeerName()).isEqualTo("Find Me");
    }

    @Test
    void testUpdateBeer() {
        Beer beer = beerRepository.save(Beer.builder()
                .beerName("Original")
                .beerStyle("Pilsner")
                .upc("1122334455")
                .price(new BigDecimal("8.99"))
                .quantityOnHand(75)
                .build());

        beer.setBeerName("Updated");
        beerRepository.save(beer);
        
        Beer updatedBeer = beerRepository.findById(beer.getId()).orElseThrow();
        assertThat(updatedBeer.getBeerName()).isEqualTo("Updated");
    }

    @Test
    void testDeleteBeer() {
        Beer beer = beerRepository.save(Beer.builder()
                .beerName("To Delete")
                .beerStyle("Stout")
                .upc("5544332211")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(25)
                .build());

        beerRepository.delete(beer);
        
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }
}
