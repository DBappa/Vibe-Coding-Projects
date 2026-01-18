package org.example.windsurfmvc;

import org.example.windsurfmvc.dtos.BeerDto;
import org.example.windsurfmvc.entities.Beer;

import java.math.BigDecimal;
import java.util.UUID;

public class TestUtils {
    public static final UUID TEST_BEER_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    public static final String TEST_BEER_NAME = "Test Beer";
    public static final String TEST_BEER_STYLE = "IPA";
    public static final String TEST_UPC = "123456789";
    public static final BigDecimal TEST_PRICE = new BigDecimal("9.99");
    public static final int TEST_QUANTITY = 100;

    public static Beer createTestBeer() {
        return Beer.builder()
                .id(TEST_BEER_ID)
                .beerName(TEST_BEER_NAME)
                .beerStyle(TEST_BEER_STYLE)
                .upc(TEST_UPC)
                .price(TEST_PRICE)
                .quantityOnHand(TEST_QUANTITY)
                .version(1)
                .build();
    }

    public static BeerDto createTestBeerDto() {
        return BeerDto.builder()
                .id(TEST_BEER_ID)
                .beerName(TEST_BEER_NAME)
                .beerStyle(TEST_BEER_STYLE)
                .upc(TEST_UPC)
                .price(TEST_PRICE)
                .quantityOnHand(TEST_QUANTITY)
                .version(1)
                .build();
    }

    public static BeerDto createUpdatedTestBeerDto() {
        return BeerDto.builder()
                .id(TEST_BEER_ID)
                .beerName("Updated " + TEST_BEER_NAME)
                .beerStyle(TEST_BEER_STYLE)
                .upc(TEST_UPC)
                .price(new BigDecimal("12.99"))
                .quantityOnHand(TEST_QUANTITY)
                .version(1)
                .build();
    }
}
