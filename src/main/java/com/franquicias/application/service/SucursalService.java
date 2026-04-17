package com.franquicias.application.service;

import com.franquicias.application.dto.SucursalDTO;
import com.franquicias.application.usecase.SucursalUseCase;
import com.franquicias.domain.model.Sucursal;
import com.franquicias.domain.repository.FranquiciaRepository;
import com.franquicias.domain.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Implementación del caso de uso para Sucursal.
 * Gestiona la lógica de negocio relacionada con sucursales.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SucursalService implements SucursalUseCase {

    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;
    private final SucursalMapper sucursalMapper;

    @Override
    public Mono<SucursalDTO> crearSucursal(String franquiciaId, SucursalDTO sucursalDTO) {
        log.debug("Creando nueva sucursal: {} para franquicia: {}", sucursalDTO.getNombre(), franquiciaId);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String nombreSucursal = Objects.requireNonNull(sucursalDTO.getNombre(), "El nombre de la sucursal es requerido");
        
        return franquiciaRepository.findById(idFranquicia)
                .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada")))
                .flatMap(franquicia -> sucursalRepository.existsByFranquiciaIdAndNombreIgnoreCase(idFranquicia, nombreSucursal)
                        .flatMap(existe -> {
                            if (Boolean.TRUE.equals(existe)) {
                                return Mono.error(new IllegalStateException("Ya existe una sucursal con ese nombre en la franquicia"));
                            }

                            Sucursal sucursal = sucursalMapper.toEntity(sucursalDTO);
                            sucursal.setFranquiciaId(idFranquicia);
                            sucursal.setFechaCreacion(LocalDateTime.now());
                            sucursal.setFechaActualizacion(LocalDateTime.now());
                            sucursal.setProductos(new ArrayList<>());
                            return sucursalRepository.save(sucursal);
                        })
                    )
                .map(sucursalMapper::toDTO)
                .doOnSuccess(dto -> log.info("Sucursal creada exitosamente: {}", dto.getId()))
                .doOnError(error -> log.error("Error al crear sucursal", error));
    }

    @Override
    public Mono<SucursalDTO> obtenerSucursal(String franquiciaId, String sucursalId) {
        log.debug("Obteniendo sucursal: {} de franquicia: {}", sucursalId, franquiciaId);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String idSucursal = Objects.requireNonNull(sucursalId, "El id de la sucursal es requerido");
        
        return sucursalRepository.findByIdAndFranquiciaId(idSucursal, idFranquicia)
                .map(sucursalMapper::toDTO)
                .doOnSuccess(dto -> log.debug("Sucursal obtenida: {}", dto.getId()))
                .doOnError(error -> log.error("Error al obtener sucursal", error));
    }

    @Override
    public Flux<SucursalDTO> obtenerSucursalesPorFranquicia(String franquiciaId) {
        log.debug("Obteniendo sucursales de franquicia: {}", franquiciaId);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        
        return sucursalRepository.findByFranquiciaId(idFranquicia)
                .map(sucursalMapper::toDTO)
                .doOnComplete(() -> log.debug("Se completó la obtención de sucursales"));
    }

    @Override
    public Mono<SucursalDTO> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre) {
        log.debug("Actualizando nombre de sucursal: {} -> {}", sucursalId, nuevoNombre);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String idSucursal = Objects.requireNonNull(sucursalId, "El id de la sucursal es requerido");
        String nombreActualizado = Objects.requireNonNull(nuevoNombre, "El nuevo nombre es requerido");
        
        return sucursalRepository.findByIdAndFranquiciaId(idSucursal, idFranquicia)
                .flatMap(sucursal -> {
                    sucursal.actualizarNombre(nombreActualizado);
                    return sucursalRepository.save(sucursal);
                })
                .map(sucursalMapper::toDTO)
                .doOnSuccess(dto -> log.info("Nombre de sucursal actualizado: {}", idSucursal))
                .doOnError(error -> log.error("Error al actualizar nombre de sucursal", error));
    }

    @Override
    public Mono<Void> eliminarSucursal(String franquiciaId, String sucursalId) {
        log.debug("Eliminando sucursal: {}", sucursalId);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String idSucursal = Objects.requireNonNull(sucursalId, "El id de la sucursal es requerido");
        
        return sucursalRepository.findByIdAndFranquiciaId(idSucursal, idFranquicia)
            .flatMap(sucursal -> sucursalRepository.deleteById(idSucursal))
            .doOnSuccess(v -> log.info("Sucursal eliminada: {}", idSucursal))
                .doOnError(error -> log.error("Error al eliminar sucursal", error));
    }
}
