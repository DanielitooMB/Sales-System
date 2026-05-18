/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.aaron.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mx.itson.aaron.db.Connection;
import mx.itson.aaron.model.Product;

/**
 *
 * @author Daniel
 */
public class ProductDao {
    
    // Obtener todos los productos ordenados por precio final ASC (via stored procedure)
    public List<Product> obtenerOrdenadosPorPrecio() {
        List<Product> productos = new ArrayList<>();
        String sql = "CALL sp_obtener_productos_ordenados()";
        try (java.sql.Connection conn = Connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
 
            while (rs.next()) {
                productos.add(mapearResultadoAProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }
 
    // Obtener productos por categoría (nombre de categoría)
    public List<Product> obtenerPorCategoria(String nombreCategoria) {
        List<Product> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, COALESCE(p.precio_oferta, p.precio) AS precio_final " +
                     "FROM productos p JOIN categorias c ON p.categoria_id = c.id " +
                     "WHERE c.nombre = ? ORDER BY precio_final ASC";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, nombreCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearResultadoAProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }
 
    // Buscar producto por nombre exacto
    public Product buscarPorNombre(String nombre) {
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, COALESCE(p.precio_oferta, p.precio) AS precio_final " +
                     "FROM productos p JOIN categorias c ON p.categoria_id = c.id WHERE p.nombre = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultadoAProduct(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    // Buscar producto por ID
    public Product buscarPorId(int id) {
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, COALESCE(p.precio_oferta, p.precio) AS precio_final " +
                     "FROM productos p JOIN categorias c ON p.categoria_id = c.id WHERE p.id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultadoAProduct(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    // Descontar stock al confirmar una compra
    public boolean descontarStock(int productoId, int cantidad) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, cantidad);
            stmt.setInt(2, productoId);
            stmt.setInt(3, cantidad);  // Evita stock negativo
 
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    // Mapear ResultSet a Product
    private Product mapearResultadoAProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setPrecioOferta(rs.getBigDecimal("precio_oferta"));
        p.setStock(rs.getInt("stock"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        if (rs.getTimestamp("created_at") != null)
            p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        if (rs.getTimestamp("updated_at") != null)
            p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return p;
    }
    
}
