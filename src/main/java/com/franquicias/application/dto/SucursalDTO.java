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
 * DTO para Sucursal.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SucursalDTO {

    private String id;

    @NotBlank(message = "El nombre de la sucursal es requerido")
    private String nombre;

    private String franquiciaId;

    private List<ProductoDTO> productos;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;
}
