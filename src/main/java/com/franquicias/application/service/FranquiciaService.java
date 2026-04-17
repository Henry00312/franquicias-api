package com.franquicias.application.service;

import com.franquicias.application.dto.FranquiciaDTO;
import com.franquicias.application.usecase.FranquiciaUseCase;
import com.franquicias.domain.model.Franquicia;
import com.franquicias.domain.repository.FranquiciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ArrayList;

/**
 * Implementación del caso de uso para Franquicia.
 * Gestiona la lógica de negocio relacionada con franquicias.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FranquiciaService implements FranquiciaUseCase {

    private final FranquiciaRepository franquiciaRepository;
    private final FranquiciaMapper franquiciaMapper;

    @Override
    public Mono<FranquiciaDTO> crearFranquicia(FranquiciaDTO franquiciaDTO) {
        log.debug("Creando nueva franquicia: {}", franquiciaDTO.getNombre());
        String nombreFranquicia = Objects.requireNonNull(franquiciaDTO.getNombre(), "El nombre de la franquicia es requerido");
        
        return franquiciaRepository.existsByNombreIgnoreCase(nombreFranquicia)
                .flatMap(existe -> {
                    if (Boolean.TRUE.equals(existe)) {
                        return Mono.error(new IllegalStateException("Ya existe una franquicia con ese nombre"));
                    }

                    Franquicia franquicia = franquiciaMapper.toEntity(franquiciaDTO);
                    franquicia.setFechaCreacion(LocalDateTime.now());
                    franquicia.setFechaActualizacion(LocalDateTime.now());
                    franquicia.setSucursales(new ArrayList<>());
                    return Mono.just(franquicia);
                })
                .flatMap(franquiciaRepository::save)
                .map(franquiciaMapper::toDTO)
                .doOnSuccess(dto -> log.info("Franquicia creada exitosamente: {}", dto.getId()))
                .doOnError(error -> log.error("Error al crear franquicia", error));
    }

    @Override
    public Mono<FranquiciaDTO> obtenerFranquicia(String id) {
        log.debug("Obteniendo franquicia con ID: {}", id);
        String franquiciaId = Objects.requireNonNull(id, "El id de la franquicia es requerido");
        
        return franquiciaRepository.findById(franquiciaId)
                .map(franquiciaMapper::toDTO)
                .doOnSuccess(dto -> log.debug("Franquicia obtenida: {}", dto.getId()))
                .doOnError(error -> log.error("Error al obtener franquicia", error));
    }

    @Override
    public Flux<FranquiciaDTO> obtenerTodasLasFranquicias() {
        log.debug("Obteniendo todas las franquicias");
        
        return franquiciaRepository.findAll()
                .map(franquiciaMapper::toDTO)
                .doOnComplete(() -> log.debug("Se completó la obtención de franquicias"));
    }

    @Override
    public Mono<FranquiciaDTO> actualizarNombreFranquicia(String id, String nuevoNombre) {
        log.debug("Actualizando nombre de franquicia: {} -> {}", id, nuevoNombre);
        String franquiciaId = Objects.requireNonNull(id, "El id de la franquicia es requerido");
        String nombreActualizado = Objects.requireNonNull(nuevoNombre, "El nuevo nombre es requerido");
        
        return franquiciaRepository.findById(franquiciaId)
                .flatMap(franquicia -> {
                    franquicia.actualizarNombre(nombreActualizado);
                    return franquiciaRepository.save(franquicia);
                })
                .map(franquiciaMapper::toDTO)
                .doOnSuccess(dto -> log.info("Nombre de franquicia actualizado: {}", franquiciaId))
                .doOnError(error -> log.error("Error al actualizar nombre de franquicia", error));
    }

    @Override
    public Mono<Void> eliminarFranquicia(String id) {
        log.debug("Eliminando franquicia: {}", id);
        String franquiciaId = Objects.requireNonNull(id, "El id de la franquicia es requerido");
        
        return franquiciaRepository.deleteById(franquiciaId)
            .doOnSuccess(v -> log.info("Franquicia eliminada: {}", franquiciaId))
                .doOnError(error -> log.error("Error al eliminar franquicia", error));
    }
}
