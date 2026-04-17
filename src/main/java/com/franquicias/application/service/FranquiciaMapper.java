package com.franquicias.application.service;

import com.franquicias.application.dto.FranquiciaDTO;
import com.franquicias.domain.model.Franquicia;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para convertir entre Franquicia y FranquiciaDTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FranquiciaMapper {

    FranquiciaDTO toDTO(Franquicia franquicia);

    Franquicia toEntity(FranquiciaDTO franquiciaDTO);
}
