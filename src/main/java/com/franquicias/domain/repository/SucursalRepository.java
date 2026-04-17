package com.franquicias.domain.repository;

import com.franquicias.domain.model.Sucursal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para Sucursal.
 * Proporciona operaciones CRUD reactivas específicas para sucursales.
 */
@Repository
public interface SucursalRepository extends ReactiveMongoRepository<Sucursal, String> {
    
    Flux<Sucursal> findByFranquiciaId(String franquiciaId);
    
    Mono<Sucursal> findByIdAndFranquiciaId(String id, String franquiciaId);

    Mono<Boolean> existsByFranquiciaIdAndNombreIgnoreCase(String franquiciaId, String nombre);
}
