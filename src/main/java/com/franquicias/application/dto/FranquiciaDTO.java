package com.franquicias.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para Franquicia.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FranquiciaDTO {

    private String id;

    @NotBlank(message = "El nombre de la franquicia es requerido")
    private String nombre;

    private List<SucursalDTO> sucursales;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;
}
