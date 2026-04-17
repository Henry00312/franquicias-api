package com.franquicias.presentation.controller;

import com.franquicias.application.dto.SucursalDTO;
import com.franquicias.application.usecase.SucursalUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

/**
 * Controlador REST para endpoints de Sucursal.
 */
@Slf4j
@RestController
@RequestMapping("/franquicias/{franquiciaId}/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalUseCase sucursalUseCase;

    /**
     * Crea una nueva sucursal en una franquicia.
     * POST /api/franquicias/{franquiciaId}/sucursales
     */
    @PostMapping
    public Mono<ResponseEntity<SucursalDTO>> crearSucursal(
            @PathVariable String franquiciaId,
            @Valid @RequestBody SucursalDTO sucursalDTO) {
        log.info("Solicitud de crear sucursal en franquicia: {}", franquiciaId);
        return sucursalUseCase.crearSucursal(franquiciaId, sucursalDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    /**
     * Obtiene una sucursal específica.
     * GET /api/franquicias/{franquiciaId}/sucursales/{sucursalId}
     */
    @GetMapping("/{sucursalId}")
    public Mono<ResponseEntity<SucursalDTO>> obtenerSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId) {
        log.info("Solicitud de obtener sucursal: {} de franquicia: {}", sucursalId, franquiciaId);
        return sucursalUseCase.obtenerSucursal(franquiciaId, sucursalId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las sucursales de una franquicia.
     * GET /api/franquicias/{franquiciaId}/sucursales
     */
    @GetMapping
    public Mono<ResponseEntity<Flux<SucursalDTO>>> obtenerSucursalesPorFranquicia(
            @PathVariable String franquiciaId) {
        log.info("Solicitud de obtener sucursales de franquicia: {}", franquiciaId);
        return Mono.just(ResponseEntity.ok(sucursalUseCase.obtenerSucursalesPorFranquicia(franquiciaId)));
    }

    /**
     * Actualiza el nombre de una sucursal.
     * PATCH /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/nombre
     */
    @PatchMapping("/{sucursalId}/nombre")
    public Mono<ResponseEntity<SucursalDTO>> actualizarNombreSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @RequestBody SucursalDTO sucursalDTO) {
        log.info("Solicitud de actualizar nombre de sucursal: {}", sucursalId);
        return sucursalUseCase.actualizarNombreSucursal(franquiciaId, sucursalId, sucursalDTO.getNombre())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una sucursal.
     * DELETE /api/franquicias/{franquiciaId}/sucursales/{sucursalId}
     */
    @DeleteMapping("/{sucursalId}")
    public Mono<ResponseEntity<String>> eliminarSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId) {
        log.info("Solicitud de eliminar sucursal: {}", sucursalId);
        return sucursalUseCase.eliminarSucursal(franquiciaId, sucursalId)
                .thenReturn(ResponseEntity.ok("Sucursal eliminada exitosamente"))
                .onErrorResume(error -> {
                    log.error("Error al eliminar sucursal", error);
                    return Mono.just(ResponseEntity.notFound().<String>build());
            });
    }
}
