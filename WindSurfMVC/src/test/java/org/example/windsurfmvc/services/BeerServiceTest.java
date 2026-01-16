package org.example.windsurfmvc.services;

import org.example.windsurfmvc.TestUtils;
import org.example.windsurfmvc.entities.Beer;
import org.example.windsurfmvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeerServiceTest {

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private BeerServiceImpl beerService;

    private Beer testBeer;
    private Beer updatedBeer;

    @BeforeEach
    void setUp() {
        testBeer = TestUtils.createTestBeer();
        testBeer.setId(1);

        updatedBeer = TestUtils.createTestBeer();
        updatedBeer.setId(1);
        updatedBeer.setBeerName("Updated Beer");
        updatedBeer.setPrice(new BigDecimal("12.99"));
    }

    @Test
    void getAllBeers() {
        // given
        when(beerRepository.findAll()).thenReturn(Arrays.asList(testBeer));

        // when
        List<Beer> beers = beerService.getAllBeers();

        // then
        assertThat(beers).hasSize(1);
        assertThat(beers.get(0).getBeerName()).isEqualTo(testBeer.getBeerName());
    }

    @Test
    void getBeerById() {
        // given
        when(beerRepository.findById(1)).thenReturn(Optional.of(testBeer));

        // when
        Beer foundBeer = beerService.getBeerById(1);

        // then
        assertThat(foundBeer).isNotNull();
        assertThat(foundBeer.getId()).isEqualTo(testBeer.getId());
    }

    @Test
    void getBeerById_NotFound() {
        // given
        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> beerService.getBeerById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Beer not found with id: 999");
    }

    @Test
    void saveBeer() {
        // given
        when(beerRepository.save(any(Beer.class))).thenReturn(testBeer);

        // when
        Beer savedBeer = beerService.saveBeer(testBeer);

        // then
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo(testBeer.getBeerName());
        verify(beerRepository, times(1)).save(any(Beer.class));
    }

    @Test
    void updateBeer() {
        // given
        when(beerRepository.findById(1)).thenReturn(Optional.of(testBeer));
        when(beerRepository.save(any(Beer.class))).thenReturn(updatedBeer);

        // when
        Beer result = beerService.updateBeer(1, updatedBeer);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBeerName()).isEqualTo(updatedBeer.getBeerName());
        assertThat(result.getPrice()).isEqualByComparingTo(updatedBeer.getPrice());
        verify(beerRepository, times(1)).save(any(Beer.class));
    }

    @Test
    void updateBeer_NotFound() {
        // given
        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> beerService.updateBeer(999, updatedBeer))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Beer not found with id: 999");
    }

    @Test
    void deleteBeer() {
        // given
        doNothing().when(beerRepository).deleteById(1);

        // when
        beerService.deleteBeer(1);

        // then
        verify(beerRepository, times(1)).deleteById(1);
    }
}
