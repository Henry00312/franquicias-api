package com.franquicias.domain.repository;

import com.franquicias.domain.model.Franquicia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para Franquicia.
 * Proporciona operaciones CRUD reactivas.
 */
@Repository
public interface FranquiciaRepository extends ReactiveMongoRepository<Franquicia, String> {
    
    Mono<Franquicia> findByNombre(String nombre);

    Mono<Boolean> existsByNombreIgnoreCase(String nombre);
}
