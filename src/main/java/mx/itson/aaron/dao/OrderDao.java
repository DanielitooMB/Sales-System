/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.aaron.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import mx.itson.aaron.db.Connection;
import mx.itson.aaron.model.Order;
import mx.itson.aaron.model.ProductOrder;

/**
 *
 * @author Daniel
 */
public class OrderDao {
 
    //Guarda la orden completa en una sola transacción:
    //Inserta en ordenes
    //Inserta cada fila en orden_productos
    //Descuenta el stock de cada producto
    //Si algo falla, hace rollback automático.
    public int insertarOrdenCompleta(Order orden, List<ProductOrder> items) {
        String sqlOrden = "INSERT INTO ordenes (cliente_id, usuario_id, total, is_active) VALUES (?, ?, ?, ?)";
        String sqlItem  = "INSERT INTO orden_productos (orden_id, producto_id, cantidad, precio) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
 
        java.sql.Connection conn = null;
        try {
            conn = Connection.getConnection();
            conn.setAutoCommit(false);
 
            //Insertar la orden
            int ordenId;
            try (PreparedStatement stmtOrden = conn.prepareStatement(sqlOrden, Statement.RETURN_GENERATED_KEYS)) {
                stmtOrden.setInt(1, orden.getClienteId());
                stmtOrden.setInt(2, orden.getUsuarioId());
                stmtOrden.setBigDecimal(3, orden.getTotal());
                stmtOrden.setBoolean(4, true);
                stmtOrden.executeUpdate();
 
                try (ResultSet keys = stmtOrden.getGeneratedKeys()) {
                    if (keys.next()) {
                        ordenId = keys.getInt(1);
                    } else {
                        conn.rollback();
                        return -1;
                    }
                }
            }
 
            //Insertar cada item y descontar stock
            try (PreparedStatement stmtItem  = conn.prepareStatement(sqlItem);
                 PreparedStatement stmtStock = conn.prepareStatement(sqlStock)) {
 
                for (ProductOrder item : items) {
                    //Insertar en orden_productos
                    stmtItem.setInt(1, ordenId);
                    stmtItem.setInt(2, item.getProductoId());
                    stmtItem.setInt(3, item.getCantidad());
                    stmtItem.setBigDecimal(4, item.getPrecio());
                    stmtItem.addBatch();
 
                    //Descontar stock (falla si no hay suficiente)
                    stmtStock.setInt(1, item.getCantidad());
                    stmtStock.setInt(2, item.getProductoId());
                    stmtStock.setInt(3, item.getCantidad());
                    int updated = stmtStock.executeUpdate();
                    if (updated == 0) {
                        //Stock insuficiente — revertir todo
                        conn.rollback();
                        return -1;
                    }
                }
                stmtItem.executeBatch();
            }
 
            conn.commit();
            return ordenId;
 
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return -1;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }
}
