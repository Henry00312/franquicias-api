package com.franquicias.presentation.controller;

import com.franquicias.application.dto.FranquiciaDTO;
import com.franquicias.application.usecase.FranquiciaUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

/**
 * Controlador REST para endpoints de Franquicia.
 */
@Slf4j
@RestController
@RequestMapping("/franquicias")
@RequiredArgsConstructor
public class FranquiciaController {

    private final FranquiciaUseCase franquiciaUseCase;

    /**
     * Crea una nueva franquicia.
     * POST /api/franquicias
     */
    @PostMapping
    public Mono<ResponseEntity<FranquiciaDTO>> crearFranquicia(@Valid @RequestBody FranquiciaDTO franquiciaDTO) {
        log.info("Solicitud de crear franquicia: {}", franquiciaDTO.getNombre());
        return franquiciaUseCase.crearFranquicia(franquiciaDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .onErrorResume(error -> {
                    log.error("Error al crear franquicia", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    /**
     * Obtiene una franquicia por ID.
     * GET /api/franquicias/{id}
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FranquiciaDTO>> obtenerFranquicia(@PathVariable String id) {
        log.info("Solicitud de obtener franquicia: {}", id);
        return franquiciaUseCase.obtenerFranquicia(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las franquicias.
     * GET /api/franquicias
     */
    @GetMapping
    public Flux<FranquiciaDTO> obtenerTodasLasFranquicias() {
        log.info("Solicitud de obtener todas las franquicias");
        return franquiciaUseCase.obtenerTodasLasFranquicias();
    }

    /**
     * Actualiza el nombre de una franquicia.
     * PATCH /api/franquicias/{id}/nombre
     */
    @PatchMapping("/{id}/nombre")
    public Mono<ResponseEntity<FranquiciaDTO>> actualizarNombreFranquicia(
            @PathVariable String id,
            @RequestBody FranquiciaDTO franquiciaDTO) {
        log.info("Solicitud de actualizar nombre de franquicia: {}", id);
        return franquiciaUseCase.actualizarNombreFranquicia(id, franquiciaDTO.getNombre())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(error -> {
                    log.error("Error al actualizar franquicia", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    /**
     * Elimina una franquicia.
     * DELETE /api/franquicias/{id}
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> eliminarFranquicia(@PathVariable String id) {
        log.info("Solicitud de eliminar franquicia: {}", id);
        return franquiciaUseCase.eliminarFranquicia(id)
            .thenReturn(ResponseEntity.ok("Franquicia eliminada exitosamente"))
            .onErrorResume(error -> {
                log.error("Error al eliminar franquicia", error);
                return Mono.just(ResponseEntity.notFound().<String>build());
            });
    }
}
