package com.franquicias.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidad de dominio Producto.
 * Representa un producto disponible en una sucursal con su stock.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "productos")
public class Producto {

    @Id
    private String id;
    
    private String nombre;
    
    private String sucursalId;
    
    private Integer stock;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;

    public void actualizarStock(Integer nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = nuevoStock;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void actualizarNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void incrementarStock(Integer cantidad) {
        this.stock += cantidad;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void decrementarStock(Integer cantidad) {
        if (this.stock < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        this.stock -= cantidad;
        this.fechaActualizacion = LocalDateTime.now();
    }
}
