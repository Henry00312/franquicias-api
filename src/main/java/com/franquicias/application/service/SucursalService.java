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
        
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada")))
                .flatMap(franquicia -> {
                    Sucursal sucursal = sucursalMapper.toEntity(sucursalDTO);
                    sucursal.setFranquiciaId(franquiciaId);
                    sucursal.setFechaCreacion(LocalDateTime.now());
                    sucursal.setFechaActualizacion(LocalDateTime.now());
                    sucursal.setProductos(new ArrayList<>());
                    return sucursalRepository.save(sucursal);
                })
                .map(sucursalMapper::toDTO)
                .doOnSuccess(dto -> log.info("Sucursal creada exitosamente: {}", dto.getId()))
                .doOnError(error -> log.error("Error al crear sucursal", error));
    }

    @Override
    public Mono<SucursalDTO> obtenerSucursal(String franquiciaId, String sucursalId) {
        log.debug("Obteniendo sucursal: {} de franquicia: {}", sucursalId, franquiciaId);
        
        return sucursalRepository.findByIdAndFranquiciaId(sucursalId, franquiciaId)
                .map(sucursalMapper::toDTO)
                .doOnSuccess(dto -> log.debug("Sucursal obtenida: {}", dto.getId()))
                .doOnError(error -> log.error("Error al obtener sucursal", error));
    }

    @Override
    public Flux<SucursalDTO> obtenerSucursalesPorFranquicia(String franquiciaId) {
        log.debug("Obteniendo sucursales de franquicia: {}", franquiciaId);
        
        return sucursalRepository.findByFranquiciaId(franquiciaId)
                .map(sucursalMapper::toDTO)
                .doOnComplete(() -> log.debug("Se completó la obtención de sucursales"));
    }

    @Override
    public Mono<SucursalDTO> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre) {
        log.debug("Actualizando nombre de sucursal: {} -> {}", sucursalId, nuevoNombre);
        
        return sucursalRepository.findByIdAndFranquiciaId(sucursalId, franquiciaId)
                .flatMap(sucursal -> {
                    sucursal.actualizarNombre(nuevoNombre);
                    return sucursalRepository.save(sucursal);
                })
                .map(sucursalMapper::toDTO)
                .doOnSuccess(dto -> log.info("Nombre de sucursal actualizado: {}", sucursalId))
                .doOnError(error -> log.error("Error al actualizar nombre de sucursal", error));
    }

    @Override
    public Mono<Void> eliminarSucursal(String franquiciaId, String sucursalId) {
        log.debug("Eliminando sucursal: {}", sucursalId);
        
        return sucursalRepository.findByIdAndFranquiciaId(sucursalId, franquiciaId)
                .flatMap(sucursal -> sucursalRepository.deleteById(sucursalId))
                .doOnSuccess(v -> log.info("Sucursal eliminada: {}", sucursalId))
                .doOnError(error -> log.error("Error al eliminar sucursal", error));
    }
}
