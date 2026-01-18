package org.example.windsurfmvc.services;

import org.example.windsurfmvc.TestUtils;
import org.example.windsurfmvc.dtos.BeerDto;
import org.example.windsurfmvc.entities.Beer;
import org.example.windsurfmvc.mappers.BeerMapper;
import org.example.windsurfmvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeerServiceTest {

    @Mock
    private BeerRepository beerRepository;

    @Spy
    private BeerMapper beerMapper = Mappers.getMapper(BeerMapper.class);

    @InjectMocks
    private BeerServiceImpl beerService;

    private Beer testBeer;
    private BeerDto testBeerDto;
    private BeerDto updatedBeerDto;

    @BeforeEach
    void setUp() {
        testBeer = TestUtils.createTestBeer();
        testBeerDto = TestUtils.createTestBeerDto();
        updatedBeerDto = TestUtils.createUpdatedTestBeerDto();
    }

    @Test
    void getAllBeers() {
        // given
        when(beerRepository.findAll()).thenReturn(Arrays.asList(testBeer));

        // when
        List<BeerDto> beers = beerService.getAllBeers();

        // then
        assertThat(beers).hasSize(1);
        assertThat(beers.get(0).getBeerName()).isEqualTo(TestUtils.TEST_BEER_NAME);
        verify(beerRepository, times(1)).findAll();
    }

    @Test
    void getBeerById() {
        // given
        when(beerRepository.findById(TestUtils.TEST_BEER_ID)).thenReturn(Optional.of(testBeer));

        // when
        BeerDto foundBeer = beerService.getBeerById(TestUtils.TEST_BEER_ID);

        // then
        assertThat(foundBeer).isNotNull();
        assertThat(foundBeer.getBeerName()).isEqualTo(TestUtils.TEST_BEER_NAME);
        verify(beerRepository, times(1)).findById(TestUtils.TEST_BEER_ID);
    }

    @Test
    void getBeerById_NotFound() {
        // given
        UUID notFoundId = UUID.randomUUID();
        when(beerRepository.findById(notFoundId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> beerService.getBeerById(notFoundId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Beer not found with id: " + notFoundId);
        verify(beerRepository, times(1)).findById(notFoundId);
    }

    @Test
    void saveBeer() {
        // given
        when(beerRepository.save(any(Beer.class))).thenReturn(testBeer);

        // when
        BeerDto savedBeer = beerService.saveBeer(testBeerDto);

        // then
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo(TestUtils.TEST_BEER_NAME);
        verify(beerRepository, times(1)).save(any(Beer.class));
    }

    @Test
    void updateBeer() {
        // given
        when(beerRepository.findById(TestUtils.TEST_BEER_ID)).thenReturn(Optional.of(testBeer));
        when(beerRepository.save(any(Beer.class))).thenReturn(testBeer);

        // when
        BeerDto result = beerService.updateBeer(TestUtils.TEST_BEER_ID, updatedBeerDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBeerName()).isEqualTo("Updated " + TestUtils.TEST_BEER_NAME);
        assertThat(result.getPrice()).isEqualByComparingTo("12.99");
        verify(beerRepository, times(1)).findById(TestUtils.TEST_BEER_ID);
        verify(beerRepository, times(1)).save(any(Beer.class));
    }

    @Test
    void updateBeer_NotFound() {
        // given
        UUID notFoundId = UUID.randomUUID();
        when(beerRepository.findById(notFoundId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> beerService.updateBeer(notFoundId, updatedBeerDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Beer not found with id: " + notFoundId);
        verify(beerRepository, times(1)).findById(notFoundId);
        verify(beerRepository, never()).save(any(Beer.class));
    }

//    @Test
//    void deleteBeer() {
//        // given
//        when(beerRepository.existsById(TestUtils.TEST_BEER_ID)).thenReturn(true);
//
//        // when
//        beerService.deleteBeer(TestUtils.TEST_BEER_ID);
//
//        // then
//        verify(beerRepository, times(1)).deleteById(TestUtils.TEST_BEER_ID);
//    }

//    @Test
//    void deleteBeer_NotFound() {
//        // given
//        UUID notFoundId = UUID.randomUUID();
//        when(beerRepository.existsById(notFoundId)).thenReturn(false);
//
//        // when & then
//        assertThatThrownBy(() -> beerService.deleteBeer(notFoundId))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Beer not found with id: " + notFoundId);
//        verify(beerRepository, never()).deleteById(any(UUID.class));
//    }
}
