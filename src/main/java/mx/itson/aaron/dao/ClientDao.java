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
import mx.itson.aaron.model.Client;

/**
 *
 * @author Daniel
 */
public class ClientDao {
    
    // Buscar cliente por ID
    public Client buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultadoAClient(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    // Buscar cliente por teléfono
    public Client buscarPorTelefono(String telefono) {
        String sql = "SELECT * FROM clientes WHERE telefono = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, telefono);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultadoAClient(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    // Obtener todos los clientes
    public List<Client> obtenerTodos() {
        List<Client> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre";
        try (java.sql.Connection conn = Connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
 
            while (rs.next()) {
                clientes.add(mapearResultadoAClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
 
    // Insertar nuevo cliente
    public boolean insertar(Client cliente) {
        String sql = "INSERT INTO clientes (nombre, email, telefono, direccion) VALUES (?, ?, ?, ?)";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getDireccion());
 
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    // Actualizar cliente
    public boolean actualizar(Client cliente) {
        String sql = "UPDATE clientes SET nombre = ?, email = ?, telefono = ?, direccion = ? WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getDireccion());
            stmt.setInt(5, cliente.getId());
 
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    // Verificar si teléfono ya existe (UNIQUE en la BD)
    public boolean existeTelefono(String telefono) {
        return buscarPorTelefono(telefono) != null;
    }
 
    // Mapear ResultSet a objeto Client
    private Client mapearResultadoAClient(ResultSet rs) throws SQLException {
        return new Client(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("telefono"),
            rs.getString("direccion"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
    }
    
}
