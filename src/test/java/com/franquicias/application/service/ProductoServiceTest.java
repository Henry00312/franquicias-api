package com.franquicias.application.service;

import com.franquicias.application.dto.ProductoDTO;
import com.franquicias.domain.model.Producto;
import com.franquicias.domain.model.Sucursal;
import com.franquicias.domain.repository.FranquiciaRepository;
import com.franquicias.domain.repository.ProductoRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para ProductoService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de ProductoService")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @Mock
    private ProductoMapper productoMapper;

    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoService = new ProductoService(productoRepository, sucursalRepository, franquiciaRepository, productoMapper);
    }

    @Test
    @DisplayName("Debe crear un producto exitosamente")
    void testCrearProductoExitoso() {
        // Arrange
        String franquiciaId = "1";
        String sucursalId = "1";
        
        ProductoDTO productoDTO = ProductoDTO.builder()
                .nombre("Producto Test")
                .stock(100)
                .build();

        Sucursal sucursal = Sucursal.builder()
                .id(sucursalId)
                .nombre("Sucursal Test")
                .franquiciaId(franquiciaId)
                .build();

        Producto productoEntity = Producto.builder()
                .id("1")
                .nombre("Producto Test")
                .sucursalId(sucursalId)
                .stock(100)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        ProductoDTO productoResponseDTO = ProductoDTO.builder()
                .id("1")
                .nombre("Producto Test")
                .stock(100)
                .build();

        when(sucursalRepository.findByIdAndFranquiciaId(sucursalId, franquiciaId)).thenReturn(Mono.just(sucursal));
        when(productoRepository.existsBySucursalIdAndNombreIgnoreCase(sucursalId, "Producto Test")).thenReturn(Mono.just(false));
        when(productoMapper.toEntity(productoDTO)).thenReturn(productoEntity);
        when(productoRepository.save(notNull())).thenReturn(Mono.just(productoEntity));
        when(productoMapper.toDTO(productoEntity)).thenReturn(productoResponseDTO);

        // Act & Assert
        StepVerifier.create(productoService.crearProducto(franquiciaId, sucursalId, productoDTO))
                .expectNextMatches(dto -> dto.getId().equals("1") && dto.getNombre().equals("Producto Test"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe fallar al crear producto cuando el nombre ya existe en la sucursal")
    void testCrearProductoDuplicado() {
        // Arrange
        String franquiciaId = "1";
        String sucursalId = "1";

        ProductoDTO productoDTO = ProductoDTO.builder()
                .nombre("Producto Test")
                .stock(100)
                .build();

        Sucursal sucursal = Sucursal.builder()
                .id(sucursalId)
                .franquiciaId(franquiciaId)
                .build();

        when(sucursalRepository.findByIdAndFranquiciaId(sucursalId, franquiciaId)).thenReturn(Mono.just(sucursal));
        when(productoRepository.existsBySucursalIdAndNombreIgnoreCase(sucursalId, "Producto Test")).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(productoService.crearProducto(franquiciaId, sucursalId, productoDTO))
                .expectErrorMatches(error -> error instanceof IllegalStateException
                        && error.getMessage().equals("Ya existe un producto con ese nombre en la sucursal"))
                .verify();
    }

    @Test
    @DisplayName("Debe actualizar el stock de un producto")
    void testActualizarStockProducto() {
        // Arrange
        String franquiciaId = "1";
        String sucursalId = "1";
        String productoId = "1";
        Integer nuevoStock = 50;

        Sucursal sucursal = Sucursal.builder()
                .id(sucursalId)
                .franquiciaId(franquiciaId)
                .build();

        Producto producto = Producto.builder()
                .id(productoId)
                .nombre("Producto Test")
                .sucursalId(sucursalId)
                .stock(100)
                .build();

        Producto productoActualizado = Producto.builder()
                .id(productoId)
                .nombre("Producto Test")
                .sucursalId(sucursalId)
                .stock(nuevoStock)
                .build();

        ProductoDTO productoDTO = ProductoDTO.builder()
                .id(productoId)
                .stock(nuevoStock)
                .build();

        when(sucursalRepository.findByIdAndFranquiciaId(sucursalId, franquiciaId)).thenReturn(Mono.just(sucursal));
        when(productoRepository.findByIdAndSucursalId(productoId, sucursalId)).thenReturn(Mono.just(producto));
        when(productoRepository.save(notNull())).thenReturn(Mono.just(productoActualizado));
        when(productoMapper.toDTO(productoActualizado)).thenReturn(productoDTO);

        // Act & Assert
        StepVerifier.create(productoService.actualizarStockProducto(franquiciaId, sucursalId, productoId, nuevoStock))
                .expectNextMatches(dto -> dto.getStock() == 50)
                .verifyComplete();
    }
}
