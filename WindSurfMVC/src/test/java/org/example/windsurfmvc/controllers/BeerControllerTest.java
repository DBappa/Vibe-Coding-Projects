package org.example.windsurfmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.windsurfmvc.TestUtils;
import org.example.windsurfmvc.dtos.BeerDto;
import org.example.windsurfmvc.entities.Beer;
import org.example.windsurfmvc.exceptions.ResourceNotFoundException;
import org.example.windsurfmvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BeerService beerService;

    private Beer testBeer;
    private BeerDto testBeerDto;
    private BeerDto updatedBeerDto;

    @BeforeEach
    void setUp() {
        testBeer = TestUtils.createTestBeer();
        testBeerDto = TestUtils.createTestBeerDto();
    }

    @Test
    void getAllBeers() throws Exception {
        // given
        List<BeerDto> beers = Arrays.asList(testBeerDto);
        given(beerService.getAllBeers()).willReturn(beers);

        // when & then
        mockMvc.perform(get("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].beerName", is(testBeerDto.getBeerName())));
    }

    @Test
    void getBeerById() throws Exception {
        // given
        given(beerService.getBeerById(TestUtils.TEST_BEER_ID)).willReturn(testBeerDto);

        // when & then
        mockMvc.perform(get("/api/v1/beers/" + TestUtils.TEST_BEER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beerName", is(testBeerDto.getBeerName())));
    }

    @Test
    void getBeerById_NotFound() throws Exception {
        // given
        UUID notFoundId = UUID.randomUUID();
        given(beerService.getBeerById(notFoundId))
                .willThrow(new ResourceNotFoundException("Beer not found with id: " + notFoundId));

        // when & then
        mockMvc.perform(get("/api/v1/beers/" + notFoundId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveBeer() throws Exception {
        // given
        given(beerService.saveBeer(any(BeerDto.class))).willReturn(testBeerDto);

        // when & then
        mockMvc.perform(post("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.beerName", is(testBeerDto.getBeerName())));
    }

//    @Test
//    void updateBeer() throws Exception {
//        // given
//        given(beerService.updateBeer(eq(TestUtils.TEST_BEER_ID), any(BeerDto.class))).willReturn(updatedBeerDto);
//
//        // when & then
//        mockMvc.perform(put("/api/v1/beers/" + TestUtils.TEST_BEER_ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedBeerDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.beerName", is(updatedBeerDto.getBeerName())));
//    }
//
//    @Test
//    void updateBeer_NotFound() throws Exception {
//        // given
//        UUID notFoundId = UUID.randomUUID();
//        given(beerService.updateBeer(eq(notFoundId), any(BeerDto.class)))
//                .willThrow(new ResourceNotFoundException("Beer not found with id: " + notFoundId));
//
//        // when & then
//        mockMvc.perform(put("/api/v1/beers/" + notFoundId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedBeerDto)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message", containsString("Beer not found with id: " + notFoundId)));
//    }
    
    @Test
    void deleteBeer() throws Exception {
        // given
        doNothing().when(beerService).deleteBeer(TestUtils.TEST_BEER_ID);

        // when & then
        mockMvc.perform(delete("/api/v1/beers/" + TestUtils.TEST_BEER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deleteBeer_NotFound() throws Exception {
        // given
        UUID notFoundId = UUID.randomUUID();
        doThrow(new ResourceNotFoundException("Beer not found with id: " + notFoundId))
                .when(beerService).deleteBeer(notFoundId);

        // when & then
        mockMvc.perform(delete("/api/v1/beers/" + notFoundId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Beer not found with id: " + notFoundId)));
    }
}
