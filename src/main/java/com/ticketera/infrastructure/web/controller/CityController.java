package com.ticketera.infrastructure.web.controller;

import java.util.List;

import com.ticketera.application.usecase.CreateCityUseCase;
import com.ticketera.application.usecase.DeleteCityUseCase;
import com.ticketera.application.usecase.GetCitiesUseCase;
import com.ticketera.application.usecase.GetCityDetailsUseCase;
import com.ticketera.application.usecase.UpdateCityUseCase;
import com.ticketera.infrastructure.web.dto.CityRequestDto;
import com.ticketera.infrastructure.web.dto.CityResponseDto;
import com.ticketera.infrastructure.web.dto.UpdateCityRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cities", description = "Administracion de ciudades")
@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CreateCityUseCase createCityUseCase;
    private final GetCitiesUseCase getCitiesUseCase;
    private final GetCityDetailsUseCase getCityDetailsUseCase;
    private final UpdateCityUseCase updateCityUseCase;
    private final DeleteCityUseCase deleteCityUseCase;

    public CityController(CreateCityUseCase createCityUseCase,
                          GetCitiesUseCase getCitiesUseCase,
                          GetCityDetailsUseCase getCityDetailsUseCase,
                          UpdateCityUseCase updateCityUseCase,
                          DeleteCityUseCase deleteCityUseCase) {
        this.createCityUseCase = createCityUseCase;
        this.getCitiesUseCase = getCitiesUseCase;
        this.getCityDetailsUseCase = getCityDetailsUseCase;
        this.updateCityUseCase = updateCityUseCase;
        this.deleteCityUseCase = deleteCityUseCase;
    }

    @Operation(summary = "Listar ciudades")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ciudades obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<CityResponseDto> listCities() {
        return getCitiesUseCase.execute().stream().map(CityResponseDto::fromDomain).toList();
    }

    @Operation(summary = "Obtener ciudad por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ciudad encontrada"),
        @ApiResponse(responseCode = "404", description = "Ciudad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public CityResponseDto getCity(@PathVariable Long id) {
        return CityResponseDto.fromDomain(getCityDetailsUseCase.execute(id));
    }

    @Operation(summary = "Crear ciudad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ciudad creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Payload invalido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<CityResponseDto> createCity(@Valid @RequestBody CityRequestDto request) {
        var city = createCityUseCase.execute(request.code(), request.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(CityResponseDto.fromDomain(city));
    }

    @Operation(summary = "Actualizar nombre de ciudad", description = "Actualiza el nombre de una ciudad existente. El campo 'code' es inmutable y no puede modificarse; para corregir un codigo, eliminar la ciudad y crear una nueva.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ciudad actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Ciudad no encontrada"),
        @ApiResponse(responseCode = "400", description = "Payload invalido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public CityResponseDto updateCity(@PathVariable Long id, @Valid @RequestBody UpdateCityRequest request) {
        return CityResponseDto.fromDomain(updateCityUseCase.execute(id, request.name()));
    }

    @Operation(summary = "Eliminar ciudad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ciudad eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Ciudad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        deleteCityUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
