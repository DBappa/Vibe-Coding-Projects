package org.example.windsurfmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.windsurfmvc.TestUtils;
import org.example.windsurfmvc.entities.Beer;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

    @BeforeEach
    void setUp() {
        testBeer = TestUtils.createTestBeer();
        testBeer.setId(1);
    }

    @Test
    void getAllBeers() throws Exception {
        // given
        List<Beer> beers = Arrays.asList(testBeer);
        given(beerService.getAllBeers()).willReturn(beers);

        // when & then
        mockMvc.perform(get("/api/beers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].beerName", is(testBeer.getBeerName())));
    }

    @Test
    void getBeerById() throws Exception {
        // given
        given(beerService.getBeerById(anyInt())).willReturn(testBeer);

        // when & then
        mockMvc.perform(get("/api/beers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBeer.getId())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void createBeer() throws Exception {
        // given
        given(beerService.saveBeer(any(Beer.class))).willReturn(testBeer);

        // when & then
        mockMvc.perform(post("/api/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void deleteBeer() throws Exception {
        // given
        doNothing().when(beerService).deleteBeer(anyInt());

        // when & then
        mockMvc.perform(delete("/api/beers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService, times(1)).deleteBeer(1);
    }
}
