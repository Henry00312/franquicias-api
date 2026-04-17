package com.franquicias.application.service;

import com.franquicias.application.dto.ProductoDTO;
import com.franquicias.domain.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para convertir entre Producto y ProductoDTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductoMapper {

    ProductoDTO toDTO(Producto producto);

    Producto toEntity(ProductoDTO productoDTO);
}
