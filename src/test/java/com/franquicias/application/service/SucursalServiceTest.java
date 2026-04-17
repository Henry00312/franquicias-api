package com.franquicias.application.service;

import com.franquicias.application.dto.SucursalDTO;
import com.franquicias.domain.model.Franquicia;
import com.franquicias.domain.model.Sucursal;
import com.franquicias.domain.repository.FranquiciaRepository;
import com.franquicias.domain.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para SucursalService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de SucursalService")
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @Mock
    private SucursalMapper sucursalMapper;

    private SucursalService sucursalService;

    @BeforeEach
    void setUp() {
        sucursalService = new SucursalService(sucursalRepository, franquiciaRepository, sucursalMapper);
    }

    @Test
    @DisplayName("Debe crear una sucursal exitosamente")
    void testCrearSucursalExitoso() {
        // Arrange
        String franquiciaId = "1";

        SucursalDTO sucursalDTO = SucursalDTO.builder()
                .nombre("Sucursal Centro")
                .build();

        Franquicia franquicia = Franquicia.builder()
                .id(franquiciaId)
                .nombre("Franquicia Test")
                .build();

        Sucursal sucursalEntity = Sucursal.builder()
                .id("1")
                .nombre("Sucursal Centro")
                .franquiciaId(franquiciaId)
                .productos(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        SucursalDTO sucursalResponseDTO = SucursalDTO.builder()
                .id("1")
                .nombre("Sucursal Centro")
                .franquiciaId(franquiciaId)
                .build();

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(sucursalRepository.existsByFranquiciaIdAndNombreIgnoreCase(franquiciaId, "Sucursal Centro")).thenReturn(Mono.just(false));
        when(sucursalMapper.toEntity(sucursalDTO)).thenReturn(sucursalEntity);
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(Mono.just(sucursalEntity));
        when(sucursalMapper.toDTO(sucursalEntity)).thenReturn(sucursalResponseDTO);

        // Act & Assert
        StepVerifier.create(sucursalService.crearSucursal(franquiciaId, sucursalDTO))
                .expectNextMatches(dto -> dto.getId().equals("1") && dto.getNombre().equals("Sucursal Centro"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe fallar al crear sucursal cuando el nombre ya existe en la franquicia")
    void testCrearSucursalDuplicada() {
        // Arrange
        String franquiciaId = "1";

        SucursalDTO sucursalDTO = SucursalDTO.builder()
                .nombre("Sucursal Centro")
                .build();

        Franquicia franquicia = Franquicia.builder()
                .id(franquiciaId)
                .nombre("Franquicia Test")
                .build();

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(sucursalRepository.existsByFranquiciaIdAndNombreIgnoreCase(franquiciaId, "Sucursal Centro")).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(sucursalService.crearSucursal(franquiciaId, sucursalDTO))
                .expectErrorMatches(error -> error instanceof IllegalStateException
                        && error.getMessage().equals("Ya existe una sucursal con ese nombre en la franquicia"))
                .verify();
    }
}
