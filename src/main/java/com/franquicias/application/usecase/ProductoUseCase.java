package com.franquicias.application.usecase;

import com.franquicias.application.dto.ProductoDTO;
import com.franquicias.application.dto.ProductoMaxStockDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz del caso de uso para Producto.
 */
public interface ProductoUseCase {

    Mono<ProductoDTO> crearProducto(String franquiciaId, String sucursalId, ProductoDTO producto);

    Mono<ProductoDTO> obtenerProducto(String franquiciaId, String sucursalId, String productoId);

    Flux<ProductoDTO> obtenerProductosPorSucursal(String franquiciaId, String sucursalId);

    Mono<ProductoDTO> actualizarStockProducto(String franquiciaId, String sucursalId, String productoId, Integer nuevoStock);

    Mono<ProductoDTO> actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nuevoNombre);

    Mono<Void> eliminarProducto(String franquiciaId, String sucursalId, String productoId);

    Flux<ProductoMaxStockDTO> obtenerProductosMaxStockPorSucursal(String franquiciaId);
}
