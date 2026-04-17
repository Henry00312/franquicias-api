package com.franquicias.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad de dominio Franquicia.
 * Representa una franquicia con su nombre y sucursales asociadas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "franquicias")
public class Franquicia {

    @Id
    private String id;
    
    private String nombre;
    
    private List<Sucursal> sucursales;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;

    public void agregarSucursal(Sucursal sucursal) {
        if (this.sucursales == null) {
            this.sucursales = new java.util.ArrayList<>();
        }
        this.sucursales.add(sucursal);
    }

    public void actualizarNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
        this.fechaActualizacion = LocalDateTime.now();
    }
}
