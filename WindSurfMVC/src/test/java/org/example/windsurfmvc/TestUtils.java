package org.example.windsurfmvc;

import org.example.windsurfmvc.entities.Beer;

import java.math.BigDecimal;

public class TestUtils {
    public static Beer createTestBeer() {
        return Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(100)
                .build();
    }
}
