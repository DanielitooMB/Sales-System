/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.aaron.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Daniel
 */
public class ProductOrder {
    
    private int id;
    private int ordenId;
    private int productoId;
    private int cantidad;
    private BigDecimal precio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductOrder() {
    }

    public ProductOrder(int id, int ordenId, int productoId, int cantidad, BigDecimal precio, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.ordenId = ordenId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precio = precio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ProductOrder(int ordenId, int productoId, int cantidad, BigDecimal precio) {
        this.ordenId = ordenId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(int ordenId) {
        this.ordenId = ordenId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public BigDecimal getSubtotal() {
        return precio.multiply(new BigDecimal(cantidad));
    }
 
    @Override
    public String toString() {
        return "OrdenProducto #" + id + " - Orden #" + ordenId + " - Producto #" + productoId;
    }
    
}
