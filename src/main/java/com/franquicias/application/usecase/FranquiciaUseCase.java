package com.franquicias.application.usecase;

import com.franquicias.application.dto.FranquiciaDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz del caso de uso para Franquicia.
 */
public interface FranquiciaUseCase {

    Mono<FranquiciaDTO> crearFranquicia(FranquiciaDTO franquicia);

    Mono<FranquiciaDTO> obtenerFranquicia(String id);

    Flux<FranquiciaDTO> obtenerTodasLasFranquicias();

    Mono<FranquiciaDTO> actualizarNombreFranquicia(String id, String nuevoNombre);

    Mono<Void> eliminarFranquicia(String id);
}
