package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.CreateCityUseCase;
import com.ticketera.application.usecase.DeleteCityUseCase;
import com.ticketera.application.usecase.GetCitiesUseCase;
import com.ticketera.application.usecase.GetCityDetailsUseCase;
import com.ticketera.application.usecase.UpdateCityUseCase;
import com.ticketera.domain.entity.City;
import com.ticketera.domain.exception.CityNotFoundException;
import com.ticketera.domain.valueobject.CityId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("City Controller")
@WebMvcTest(CityController.class)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateCityUseCase createCityUseCase;

    @MockitoBean
    private GetCitiesUseCase getCitiesUseCase;

    @MockitoBean
    private GetCityDetailsUseCase getCityDetailsUseCase;

    @MockitoBean
    private UpdateCityUseCase updateCityUseCase;

    @MockitoBean
    private DeleteCityUseCase deleteCityUseCase;

    @Test
    @DisplayName("Lists all cities")
    void listsAllCities() throws Exception {
        when(getCitiesUseCase.execute()).thenReturn(List.of(
            new City(new CityId("LIM"), "Lima")));

        mockMvc.perform(get("/api/v1/cities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("LIM"))
            .andExpect(jsonPath("$[0].name").value("Lima"));
    }

    @Test
    @DisplayName("Returns city by id")
    void returnsCityById() throws Exception {
        when(getCityDetailsUseCase.execute("LIM"))
            .thenReturn(new City(new CityId("LIM"), "Lima"));

        mockMvc.perform(get("/api/v1/cities/LIM"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("LIM"))
            .andExpect(jsonPath("$.name").value("Lima"));
    }

    @Test
    @DisplayName("Returns 404 when city not found")
    void returns404WhenCityNotFound() throws Exception {
        when(getCityDetailsUseCase.execute("MISS"))
            .thenThrow(new CityNotFoundException("City not found: MISS"));

        mockMvc.perform(get("/api/v1/cities/MISS"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("Creates city and returns 201")
    void createsCityAndReturns201() throws Exception {
        when(createCityUseCase.execute(anyString(), anyString()))
            .thenReturn(new City(new CityId("LIM"), "Lima"));

        mockMvc.perform(post("/api/v1/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"id": "LIM", "name": "Lima"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("LIM"))
            .andExpect(jsonPath("$.name").value("Lima"));
    }

    @Test
    @DisplayName("Updates city successfully")
    void updatesCitySuccessfully() throws Exception {
        when(updateCityUseCase.execute("LIM", "Lima Metropolitana"))
            .thenReturn(new City(new CityId("LIM"), "Lima Metropolitana"));

        mockMvc.perform(put("/api/v1/cities/LIM")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"id": "LIM", "name": "Lima Metropolitana"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Lima Metropolitana"));
    }

    @Test
    @DisplayName("Returns 404 when updating non-existent city")
    void returns404WhenUpdatingNonExistentCity() throws Exception {
        when(updateCityUseCase.execute(anyString(), anyString()))
            .thenThrow(new CityNotFoundException("City with id 'MISS' not found"));

        mockMvc.perform(put("/api/v1/cities/MISS")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"id": "MISS", "name": "New Name"}
                    """))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("Deletes city and returns 204")
    void deletesCityAndReturns204() throws Exception {
        mockMvc.perform(delete("/api/v1/cities/LIM"))
            .andExpect(status().isNoContent());

        verify(deleteCityUseCase).execute("LIM");
    }
}
