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
public class Order {
    
    private int id;
    private int clienteId;
    private int usuarioId;
    private BigDecimal total;
    private boolean isActive;
    private LocalDateTime createdAt;

    public Order() {
    }

    public Order(int id, int clienteId, int usuarioId, BigDecimal total, boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.clienteId = clienteId;
        this.usuarioId = usuarioId;
        this.total = total;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public Order(int clienteId, int usuarioId, BigDecimal total, boolean isActive) {
        this.clienteId = clienteId;
        this.usuarioId = usuarioId;
        this.total = total;
        this.isActive = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Orden #" + id + " - Total: $" + total;
    }
    
}
