package com.franquicias.application.service;

import com.franquicias.application.dto.FranquiciaDTO;
import com.franquicias.domain.model.Franquicia;
import com.franquicias.domain.repository.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para FranquiciaService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de FranquiciaService")
class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @Mock
    private FranquiciaMapper franquiciaMapper;

    private FranquiciaService franquiciaService;

    @BeforeEach
    void setUp() {
        franquiciaService = new FranquiciaService(franquiciaRepository, franquiciaMapper);
    }

    @Test
    @DisplayName("Debe crear una franquicia exitosamente")
    void testCrearFranquiciaExitoso() {
        // Arrange
        FranquiciaDTO franquiciaDTO = FranquiciaDTO.builder()
                .nombre("Franquicia Test")
                .build();

        Franquicia franquiciaEntity = Franquicia.builder()
                .id("1")
                .nombre("Franquicia Test")
                .sucursales(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        FranquiciaDTO franquiciaResponseDTO = FranquiciaDTO.builder()
                .id("1")
                .nombre("Franquicia Test")
                .build();

        when(franquiciaMapper.toEntity(franquiciaDTO)).thenReturn(franquiciaEntity);
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquiciaEntity));
        when(franquiciaMapper.toDTO(franquiciaEntity)).thenReturn(franquiciaResponseDTO);

        // Act & Assert
        StepVerifier.create(franquiciaService.crearFranquicia(franquiciaDTO))
                .expectNextMatches(dto -> dto.getId().equals("1") && dto.getNombre().equals("Franquicia Test"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener una franquicia por ID")
    void testObtenerFranquiciaExitoso() {
        // Arrange
        String franquiciaId = "1";
        Franquicia franquicia = Franquicia.builder()
                .id(franquiciaId)
                .nombre("Franquicia Test")
                .build();

        FranquiciaDTO franquiciaDTO = FranquiciaDTO.builder()
                .id(franquiciaId)
                .nombre("Franquicia Test")
                .build();

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(franquiciaMapper.toDTO(franquicia)).thenReturn(franquiciaDTO);

        // Act & Assert
        StepVerifier.create(franquiciaService.obtenerFranquicia(franquiciaId))
                .expectNextMatches(dto -> dto.getId().equals(franquiciaId))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener todas las franquicias")
    void testObtenerTodasLasFranquicias() {
        // Arrange
        Franquicia franquicia1 = Franquicia.builder()
                .id("1")
                .nombre("Franquicia 1")
                .build();

        Franquicia franquicia2 = Franquicia.builder()
                .id("2")
                .nombre("Franquicia 2")
                .build();

        FranquiciaDTO dto1 = FranquiciaDTO.builder().id("1").nombre("Franquicia 1").build();
        FranquiciaDTO dto2 = FranquiciaDTO.builder().id("2").nombre("Franquicia 2").build();

        when(franquiciaRepository.findAll()).thenReturn(Flux.just(franquicia1, franquicia2));
        when(franquiciaMapper.toDTO(franquicia1)).thenReturn(dto1);
        when(franquiciaMapper.toDTO(franquicia2)).thenReturn(dto2);

        // Act & Assert
        StepVerifier.create(franquiciaService.obtenerTodasLasFranquicias())
                .expectNext(dto1, dto2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe actualizar el nombre de una franquicia")
    void testActualizarNombreFranquicia() {
        // Arrange
        String franquiciaId = "1";
        String nuevoNombre = "Nuevo Nombre";

        Franquicia franquicia = Franquicia.builder()
                .id(franquiciaId)
                .nombre("Nombre Antiguo")
                .build();

        Franquicia franquiciaActualizada = Franquicia.builder()
                .id(franquiciaId)
                .nombre(nuevoNombre)
                .build();

        FranquiciaDTO franquiciaDTO = FranquiciaDTO.builder()
                .id(franquiciaId)
                .nombre(nuevoNombre)
                .build();

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenReturn(Mono.just(franquiciaActualizada));
        when(franquiciaMapper.toDTO(franquiciaActualizada)).thenReturn(franquiciaDTO);

        // Act & Assert
        StepVerifier.create(franquiciaService.actualizarNombreFranquicia(franquiciaId, nuevoNombre))
                .expectNextMatches(dto -> dto.getNombre().equals(nuevoNombre))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe eliminar una franquicia")
    void testEliminarFranquicia() {
        // Arrange
        String franquiciaId = "1";

        when(franquiciaRepository.deleteById(franquiciaId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(franquiciaService.eliminarFranquicia(franquiciaId))
                .verifyComplete();
    }
}
