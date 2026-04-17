package com.franquicias.presentation.controller;

import com.franquicias.application.dto.ProductoMaxStockDTO;
import com.franquicias.application.usecase.ProductoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * Controlador REST para endpoints adicionales de Franquicia.
 */
@Slf4j
@RestController
@RequestMapping("/franquicias")
@RequiredArgsConstructor
public class FranquiciaReporteController {

    private final ProductoUseCase productoUseCase;

    /**
     * Obtiene los productos con máximo stock por sucursal de una franquicia.
     * GET /api/franquicias/{franquiciaId}/productos-max-stock
     */
    @GetMapping("/{franquiciaId}/productos-max-stock")
    public Flux<ProductoMaxStockDTO> obtenerProductosMaxStockPorSucursal(
            @PathVariable String franquiciaId) {
        log.info("Solicitud de obtener productos máximos por sucursal de franquicia: {}", franquiciaId);
        return productoUseCase.obtenerProductosMaxStockPorSucursal(franquiciaId);
    }
}
