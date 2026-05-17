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
public class Payment {
    
    private int id;
    private int ordenId;
    private BigDecimal monto;
    private String metodoPago;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Payment() {
    }

    public Payment(int id, int ordenId, BigDecimal monto, String metodoPago, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.ordenId = ordenId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Payment(int ordenId, BigDecimal monto, String metodoPago) {
        this.ordenId = ordenId;
        this.monto = monto;
        this.metodoPago = metodoPago;
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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
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
    
    @Override
    public String toString() {
        return "Pago #" + id + " - Orden #" + ordenId + " - $" + monto + " (" + metodoPago + ")";
    }
    
}
