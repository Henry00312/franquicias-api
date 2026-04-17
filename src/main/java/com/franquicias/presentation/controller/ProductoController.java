package com.franquicias.presentation.controller;

import com.franquicias.application.dto.ProductoDTO;
import com.franquicias.application.usecase.ProductoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

/**
 * Controlador REST para endpoints de Producto.
 */
@Slf4j
@RestController
@RequestMapping("/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoUseCase productoUseCase;

    /**
     * Crea un nuevo producto en una sucursal.
     * POST /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos
     */
    @PostMapping
    public Mono<ResponseEntity<ProductoDTO>> crearProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @Valid @RequestBody ProductoDTO productoDTO) {
        log.info("Solicitud de crear producto en sucursal: {}", sucursalId);
        return productoUseCase.crearProducto(franquiciaId, sucursalId, productoDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .onErrorResume(error -> {
                    log.error("Error al crear producto", error);
                    if (error instanceof IllegalStateException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    /**
     * Obtiene un producto específico.
     * GET /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}
     */
    @GetMapping("/{productoId}")
    public Mono<ResponseEntity<ProductoDTO>> obtenerProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId) {
        log.info("Solicitud de obtener producto: {}", productoId);
        return productoUseCase.obtenerProducto(franquiciaId, sucursalId, productoId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los productos de una sucursal.
     * GET /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos
     */
    @GetMapping
    public Mono<ResponseEntity<Flux<ProductoDTO>>> obtenerProductosPorSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId) {
        log.info("Solicitud de obtener productos de sucursal: {}", sucursalId);
        return Mono.just(ResponseEntity.ok(productoUseCase.obtenerProductosPorSucursal(franquiciaId, sucursalId)));
    }

    /**
     * Actualiza el stock de un producto.
     * PATCH /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock
     */
    @PatchMapping("/{productoId}/stock")
    public Mono<ResponseEntity<ProductoDTO>> actualizarStockProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId,
            @RequestBody ProductoDTO productoDTO) {
        log.info("Solicitud de actualizar stock de producto: {}", productoId);
        return productoUseCase.actualizarStockProducto(franquiciaId, sucursalId, productoId, productoDTO.getStock())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(error -> {
                    log.error("Error al actualizar stock", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    /**
     * Actualiza el nombre de un producto.
     * PATCH /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/nombre
     */
    @PatchMapping("/{productoId}/nombre")
    public Mono<ResponseEntity<ProductoDTO>> actualizarNombreProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId,
            @RequestBody ProductoDTO productoDTO) {
        log.info("Solicitud de actualizar nombre de producto: {}", productoId);
        return productoUseCase.actualizarNombreProducto(franquiciaId, sucursalId, productoId, productoDTO.getNombre())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(error -> {
                    log.error("Error al actualizar nombre", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    /**
     * Elimina un producto de una sucursal.
     * DELETE /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}
     */
    @DeleteMapping("/{productoId}")
    public Mono<ResponseEntity<String>> eliminarProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId) {
        log.info("Solicitud de eliminar producto: {}", productoId);
        return productoUseCase.eliminarProducto(franquiciaId, sucursalId, productoId)
                .thenReturn(ResponseEntity.ok("Producto eliminado exitosamente"))
                .onErrorResume(error -> {
                    log.error("Error al eliminar producto", error);
                    return Mono.just(ResponseEntity.notFound().<String>build());
            });
    }

}
