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
 
    //Fallback SQL
    private static final String SQL_TODOS_ORDENADOS =
        "SELECT p.*, c.nombre AS categoria_nombre, " +
        "       COALESCE(p.precio_oferta, p.precio) AS precio_final " +
        "FROM productos p " +
        "JOIN categorias c ON p.categoria_id = c.id " +
        "ORDER BY precio_final ASC";
    
    //Obtiene todos los productos ordenados de menor a mayor precio_final.
    //Intenta el Stored Procedure primero; si falla,
    //ejecuta la consulta SQL directa equivalente.
    public List<Product> obtenerOrdenadosPorPrecio() {
        List<Product> productos = new ArrayList<>();
 
        try (java.sql.Connection conn = Connection.getConnection()) {
 
            //primer intento: Stored Procedure
            try (Statement stmt = conn.createStatement();
                 ResultSet rs   = stmt.executeQuery("CALL sp_obtener_productos_ordenados()")) {
 
                while (rs.next()) {
                    productos.add(mapearResultadoAProduct(rs));
                }
                System.out.println("Productos cargados via Stored Procedure (" + productos.size() + ")");
 
            } catch (SQLException spEx) {
                // SP no existe aun → usar SQL directo
                System.out.println("SP no disponible, usando SQL directo: " + spEx.getMessage());
                productos.clear();
 
                try (Statement stmt2 = conn.createStatement();
                     ResultSet rs2   = stmt2.executeQuery(SQL_TODOS_ORDENADOS)) {
 
                    while (rs2.next()) {
                        productos.add(mapearResultadoAProduct(rs2));
                    }
                    System.out.println("Productos cargados via SQL directo (" + productos.size() + ")");
                }
            }
 
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
        }
 
        return productos;
    }
 
    //Obtener productos por categoria (nombre de categoria)
    public List<Product> obtenerPorCategoria(String nombreCategoria) {
        List<Product> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, " +
                     "COALESCE(p.precio_oferta, p.precio) AS precio_final " +
                     "FROM productos p JOIN categorias c ON p.categoria_id = c.id " +
                     "WHERE c.nombre = ? ORDER BY precio_final ASC";
 
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, nombreCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) productos.add(mapearResultadoAProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }
 
    //Buscar producto por nombre exacto
    public Product buscarPorNombre(String nombre) {
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, " +
                     "COALESCE(p.precio_oferta, p.precio) AS precio_final " +
                     "FROM productos p JOIN categorias c ON p.categoria_id = c.id " +
                     "WHERE LOWER(p.nombre) = LOWER(?)";
 
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, nombre.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearResultadoAProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    //Buscar producto por ID
    public Product buscarPorId(int id) {
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, " +
                     "COALESCE(p.precio_oferta, p.precio) AS precio_final " +
                     "FROM productos p JOIN categorias c ON p.categoria_id = c.id " +
                     "WHERE p.id = ?";
 
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapearResultadoAProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    //Insertar nuevo producto
    public boolean insertar(Product producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, precio_oferta, stock, categoria_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setBigDecimal(3, producto.getPrecio());
            stmt.setBigDecimal(4, producto.getPrecioOferta());
            stmt.setInt(5, producto.getStock());
            stmt.setInt(6, producto.getCategoriaId());
            return stmt.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    //Actualizar producto existente
    public boolean actualizar(Product producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, " +
                     "precio_oferta = ?, stock = ?, categoria_id = ? WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setBigDecimal(3, producto.getPrecio());
            stmt.setBigDecimal(4, producto.getPrecioOferta());
            stmt.setInt(5, producto.getStock());
            stmt.setInt(6, producto.getCategoriaId());
            stmt.setInt(7, producto.getId());
            return stmt.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    //Eliminar producto por ID
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    //Descontar stock al confirmar una compra (usado por OrderDao en transaccion)
    public boolean descontarStock(int productoId, int cantidad) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, cantidad);
            stmt.setInt(2, productoId);
            stmt.setInt(3, cantidad);
            return stmt.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    //Verificar si nombre ya existe
    public boolean existeNombre(String nombre) {
        return buscarPorNombre(nombre) != null;
    }
 
    //Mapear ResultSet = Product
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
