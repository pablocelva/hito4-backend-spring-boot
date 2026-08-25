package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.CreateCityUseCase;
import com.ticketera.application.usecase.DeleteCityUseCase;
import com.ticketera.application.usecase.GetCitiesUseCase;
import com.ticketera.application.usecase.GetCityDetailsUseCase;
import com.ticketera.application.usecase.UpdateCityUseCase;
import com.ticketera.domain.entity.City;
import com.ticketera.domain.exception.CityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
            new City(1L, "LIM", "Lima")));

        mockMvc.perform(get("/api/v1/cities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].code").value("LIM"))
            .andExpect(jsonPath("$[0].name").value("Lima"));
    }

    @Test
    @DisplayName("Returns city by id")
    void returnsCityById() throws Exception {
        when(getCityDetailsUseCase.execute(1L))
            .thenReturn(new City(1L, "LIM", "Lima"));

        mockMvc.perform(get("/api/v1/cities/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.code").value("LIM"))
            .andExpect(jsonPath("$.name").value("Lima"));
    }

    @Test
    @DisplayName("Returns 404 when city not found")
    void returns404WhenCityNotFound() throws Exception {
        when(getCityDetailsUseCase.execute(999L))
            .thenThrow(new CityNotFoundException("City not found: 999"));

        mockMvc.perform(get("/api/v1/cities/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("Creates city and returns 201")
    void createsCityAndReturns201() throws Exception {
        when(createCityUseCase.execute("LIM", "Lima"))
            .thenReturn(new City(1L, "LIM", "Lima"));

        mockMvc.perform(post("/api/v1/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"code": "LIM", "name": "Lima"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.code").value("LIM"))
            .andExpect(jsonPath("$.name").value("Lima"));
    }

    @Test
    @DisplayName("Updates city successfully")
    void updatesCitySuccessfully() throws Exception {
        when(updateCityUseCase.execute(1L, "Lima Metropolitana"))
            .thenReturn(new City(1L, "LIM", "Lima Metropolitana"));

        mockMvc.perform(put("/api/v1/cities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"code": "LIM", "name": "Lima Metropolitana"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Lima Metropolitana"));
    }

    @Test
    @DisplayName("Returns 404 when updating non-existent city")
    void returns404WhenUpdatingNonExistentCity() throws Exception {
        when(updateCityUseCase.execute(any(), any()))
            .thenThrow(new CityNotFoundException("City with id '999' not found"));

        mockMvc.perform(put("/api/v1/cities/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"code": "MISS", "name": "New Name"}
                    """))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("Deletes city and returns 204")
    void deletesCityAndReturns204() throws Exception {
        mockMvc.perform(delete("/api/v1/cities/1"))
            .andExpect(status().isNoContent());

        verify(deleteCityUseCase).execute(1L);
    }
}
