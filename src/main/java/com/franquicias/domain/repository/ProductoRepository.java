package com.franquicias.domain.repository;

import com.franquicias.domain.model.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para Producto.
 * Proporciona operaciones CRUD reactivas específicas para productos.
 */
@Repository
public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {
    
    Flux<Producto> findBySucursalId(String sucursalId);
    
    Mono<Producto> findByIdAndSucursalId(String id, String sucursalId);
    
    Mono<Producto> findFirstBySucursalIdOrderByStockDesc(String sucursalId);
}
