package com.franquicias.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mostrar el producto con más stock por sucursal.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductoMaxStockDTO {

    private String productoNombre;

    private String sucursalNombre;

    private String sucursalId;

    private Integer stock;
}
