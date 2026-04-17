package com.franquicias.application.service;

import com.franquicias.application.dto.ProductoDTO;
import com.franquicias.application.dto.ProductoMaxStockDTO;
import com.franquicias.application.usecase.ProductoUseCase;
import com.franquicias.domain.model.Producto;
import com.franquicias.domain.model.Sucursal;
import com.franquicias.domain.repository.FranquiciaRepository;
import com.franquicias.domain.repository.ProductoRepository;
import com.franquicias.domain.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Implementación del caso de uso para Producto.
 * Gestiona la lógica de negocio relacionada con productos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService implements ProductoUseCase {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;
    private final ProductoMapper productoMapper;

    @Override
    public Mono<ProductoDTO> crearProducto(String franquiciaId, String sucursalId, ProductoDTO productoDTO) {
        log.debug("Creando nuevo producto: {} para sucursal: {}", productoDTO.getNombre(), sucursalId);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String idSucursal = Objects.requireNonNull(sucursalId, "El id de la sucursal es requerido");
        String nombreProducto = Objects.requireNonNull(productoDTO.getNombre(), "El nombre del producto es requerido");
        
        return validarSucursalPerteneceFranquicia(idFranquicia, idSucursal)
                .flatMap(sucursal -> productoRepository.existsBySucursalIdAndNombreIgnoreCase(idSucursal, nombreProducto)
                        .flatMap(existe -> {
                            if (Boolean.TRUE.equals(existe)) {
                                return Mono.error(new IllegalStateException("Ya existe un producto con ese nombre en la sucursal"));
                            }

                            Producto producto = productoMapper.toEntity(productoDTO);
                            producto.setSucursalId(idSucursal);
                            producto.setFechaCreacion(LocalDateTime.now());
                            producto.setFechaActualizacion(LocalDateTime.now());
                            return productoRepository.save(producto);
                        })
                    )
                .map(productoMapper::toDTO)
                .doOnSuccess(dto -> log.info("Producto creado exitosamente: {}", dto.getId()))
                .doOnError(error -> log.error("Error al crear producto", error));
    }

    @Override
    public Mono<ProductoDTO> obtenerProducto(String franquiciaId, String sucursalId, String productoId) {
        log.debug("Obteniendo producto: {}", productoId);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String idSucursal = Objects.requireNonNull(sucursalId, "El id de la sucursal es requerido");
        String idProducto = Objects.requireNonNull(productoId, "El id del producto es requerido");
        
        return validarSucursalPerteneceFranquicia(idFranquicia, idSucursal)
            .flatMap(sucursal -> productoRepository.findByIdAndSucursalId(idProducto, idSucursal))
                .map(productoMapper::toDTO)
                .doOnError(error -> log.error("Error al obtener producto", error));
    }

    @Override
    public Flux<ProductoDTO> obtenerProductosPorSucursal(String franquiciaId, String sucursalId) {
        log.debug("Obteniendo productos de sucursal: {}", sucursalId);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String idSucursal = Objects.requireNonNull(sucursalId, "El id de la sucursal es requerido");
        
        return validarSucursalPerteneceFranquicia(idFranquicia, idSucursal)
            .flatMapMany(sucursal -> productoRepository.findBySucursalId(idSucursal))
                .map(productoMapper::toDTO)
                .doOnComplete(() -> log.debug("Se completó la obtención de productos"));
    }

    @Override
    public Mono<ProductoDTO> actualizarStockProducto(String franquiciaId, String sucursalId, String productoId, Integer nuevoStock) {
        log.debug("Actualizando stock de producto: {} -> {}", productoId, nuevoStock);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String idSucursal = Objects.requireNonNull(sucursalId, "El id de la sucursal es requerido");
        String idProducto = Objects.requireNonNull(productoId, "El id del producto es requerido");
        
        return validarSucursalPerteneceFranquicia(idFranquicia, idSucursal)
                .flatMap(sucursal -> productoRepository.findByIdAndSucursalId(idProducto, idSucursal))
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado")))
                .flatMap(producto -> {
                    producto.actualizarStock(nuevoStock);
                    return productoRepository.save(producto);
                })
                .map(productoMapper::toDTO)
                .doOnSuccess(dto -> log.info("Stock del producto actualizado: {}", idProducto))
                .doOnError(error -> log.error("Error al actualizar stock", error));
    }

    @Override
    public Mono<ProductoDTO> actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nuevoNombre) {
        log.debug("Actualizando nombre de producto: {} -> {}", productoId, nuevoNombre);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String idSucursal = Objects.requireNonNull(sucursalId, "El id de la sucursal es requerido");
        String idProducto = Objects.requireNonNull(productoId, "El id del producto es requerido");
        String nombreActualizado = Objects.requireNonNull(nuevoNombre, "El nuevo nombre es requerido");
        
        return validarSucursalPerteneceFranquicia(idFranquicia, idSucursal)
                .flatMap(sucursal -> productoRepository.findByIdAndSucursalId(idProducto, idSucursal))
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado")))
                .flatMap(producto -> {
                    producto.actualizarNombre(nombreActualizado);
                    return productoRepository.save(producto);
                })
                .map(productoMapper::toDTO)
                .doOnSuccess(dto -> log.info("Nombre del producto actualizado: {}", idProducto))
                .doOnError(error -> log.error("Error al actualizar nombre", error));
    }

    @Override
    public Mono<Void> eliminarProducto(String franquiciaId, String sucursalId, String productoId) {
        log.debug("Eliminando producto: {}", productoId);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String idSucursal = Objects.requireNonNull(sucursalId, "El id de la sucursal es requerido");
        String idProducto = Objects.requireNonNull(productoId, "El id del producto es requerido");
        
        return validarSucursalPerteneceFranquicia(idFranquicia, idSucursal)
            .flatMap(sucursal -> productoRepository.findByIdAndSucursalId(idProducto, idSucursal))
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado")))
            .flatMap(producto -> productoRepository.deleteById(idProducto))
            .doOnSuccess(v -> log.info("Producto eliminado: {}", idProducto))
                .doOnError(error -> log.error("Error al eliminar producto", error));
    }

    @Override
    public Flux<ProductoMaxStockDTO> obtenerProductosMaxStockPorSucursal(String franquiciaId) {
        log.debug("Obteniendo productos con máximo stock por sucursal de franquicia: {}", franquiciaId);
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        
        return franquiciaRepository.findById(idFranquicia)
                .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada")))
            .flatMapMany(franquicia -> sucursalRepository.findByFranquiciaId(idFranquicia))
                .flatMap(sucursal -> productoRepository.findFirstBySucursalIdOrderByStockDesc(sucursal.getId())
                        .map(producto -> ProductoMaxStockDTO.builder()
                                .productoNombre(producto.getNombre())
                                .sucursalNombre(sucursal.getNombre())
                                .sucursalId(sucursal.getId())
                                .stock(producto.getStock())
                                .build())
                        .defaultIfEmpty(ProductoMaxStockDTO.builder()
                                .sucursalNombre(sucursal.getNombre())
                                .sucursalId(sucursal.getId())
                                .build()
                        ))
                .doOnComplete(() -> log.debug("Se completó la obtención de productos máximos"));
    }

    private Mono<Sucursal> validarSucursalPerteneceFranquicia(String franquiciaId, String sucursalId) {
        String idFranquicia = Objects.requireNonNull(franquiciaId, "El id de la franquicia es requerido");
        String idSucursal = Objects.requireNonNull(sucursalId, "El id de la sucursal es requerido");

        return sucursalRepository.findByIdAndFranquiciaId(idSucursal, idFranquicia)
                .switchIfEmpty(Mono.error(new RuntimeException("Sucursal no pertenece a la franquicia")));
    }
}
