package com.franquicias.application.service;

import com.franquicias.application.dto.SucursalDTO;
import com.franquicias.domain.model.Sucursal;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para convertir entre Sucursal y SucursalDTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SucursalMapper {

    SucursalDTO toDTO(Sucursal sucursal);

    Sucursal toEntity(SucursalDTO sucursalDTO);
}
