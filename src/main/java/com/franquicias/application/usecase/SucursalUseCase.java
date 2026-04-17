package com.franquicias.application.usecase;

import com.franquicias.application.dto.SucursalDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz del caso de uso para Sucursal.
 */
public interface SucursalUseCase {

    Mono<SucursalDTO> crearSucursal(String franquiciaId, SucursalDTO sucursal);

    Mono<SucursalDTO> obtenerSucursal(String franquiciaId, String sucursalId);

    Flux<SucursalDTO> obtenerSucursalesPorFranquicia(String franquiciaId);

    Mono<SucursalDTO> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre);

    Mono<Void> eliminarSucursal(String franquiciaId, String sucursalId);
}
