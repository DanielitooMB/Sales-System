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
public class Product {
    
    private int id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal precioOferta;
    private int stock;
    private int categoriaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
 
    public Product() {
    }
 
    public Product(int id, String nombre, String descripcion, BigDecimal precio, BigDecimal precioOferta, int stock, int categoriaId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.precioOferta = precioOferta;
        this.stock = stock;
        this.categoriaId = categoriaId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
 
    public Product(String nombre, String descripcion, BigDecimal precio, BigDecimal precioOferta, int stock, int categoriaId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.precioOferta = precioOferta;
        this.stock = stock;
        this.categoriaId = categoriaId;
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
    public String getDescripcion() {
        return descripcion;
    }
 
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
 
    // Getter del precio base (sin oferta)
    public BigDecimal getPrecio() {
        return precio;
    }
 
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
 
    public BigDecimal getPrecioOferta() {
        return precioOferta;
    }
 
    public void setPrecioOferta(BigDecimal precioOferta) {
        this.precioOferta = precioOferta;
    }
 
    //Devuelve precio_oferta si existe, si no devuelve precio base
    public BigDecimal getPrecioFinal() {
        if (precioOferta != null && precioOferta.compareTo(BigDecimal.ZERO) > 0) {
            return precioOferta;
        } else {
            return precio;
        }
    }
 
    public int getStock() {
        return stock;
    }
 
    public void setStock(int stock) {
        this.stock = stock;
    }
 
    public int getCategoriaId() {
        return categoriaId;
    }
 
    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
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
    
    public boolean tieneStock() {
        return stock > 0;
    }
    
    public boolean tieneStockSuficiente(int cantidad) {
        return stock >= cantidad;
    }
    
    @Override
    public String toString() {
        return nombre + " - $" + getPrecioFinal();
    }
    
}
