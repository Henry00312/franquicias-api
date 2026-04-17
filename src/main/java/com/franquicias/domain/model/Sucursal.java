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
 * Entidad de dominio Sucursal.
 * Representa una sucursal de una franquicia con sus productos asociados.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "sucursales")
public class Sucursal {

    @Id
    private String id;
    
    private String nombre;
    
    private String franquiciaId;
    
    private List<Producto> productos;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;

    public void agregarProducto(Producto producto) {
        if (this.productos == null) {
            this.productos = new java.util.ArrayList<>();
        }
        this.productos.add(producto);
    }

    public void actualizarNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void removerProducto(String productoId) {
        if (this.productos != null) {
            this.productos.removeIf(p -> p.getId().equals(productoId));
        }
    }
}
