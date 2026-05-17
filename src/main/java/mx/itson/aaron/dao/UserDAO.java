/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.aaron.dao;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import mx.itson.aaron.db.Connection;
import mx.itson.aaron.model.User;

/**
 *
 * @author Daniel
 */
public class UserDAO {
    
    //Buscar usuario por EMAIL
    public User buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultadoAUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Buscar usuario por ID
    public User buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultadoAUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Obtener todos los usuarios
    public List<User> obtenerTodos() {
        List<User> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nombre";
        try (java.sql.Connection conn = Connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapearResultadoAUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    //Obtener solo usuarios ACTIVOS
    public List<User> obtenerActivos() {
        List<User> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE is_active = true ORDER BY nombre";
        try (java.sql.Connection conn = Connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapearResultadoAUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
    
    //Insertar nuevo usuario de CREAR CUENTA
    public boolean insertar(User usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, password_hash, is_active, rol) VALUES (?, ?, ?, ?, ?)";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPasswordHash());
            stmt.setBoolean(4, usuario.isIsActive());
            stmt.setString(5, usuario.getRol());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Actualizar usuario
    public boolean actualizar(User usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, password_hash = ?, is_active = ?, rol = ? WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPasswordHash());
            stmt.setBoolean(4, usuario.isIsActive());
            stmt.setString(5, usuario.getRol());
            stmt.setInt(6, usuario.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Desactivar usuario
    public boolean desactivar(int id) {
        String sql = "UPDATE usuarios SET is_active = false WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Verificar si email ya existe
    public boolean existeEmail(String email) {
        return buscarPorEmail(email) != null;
    }
    
    //Mapear ResultSet a objeto User
    private User mapearResultadoAUser(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("password_hash"),
            rs.getBoolean("is_active"),
            rs.getString("rol"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
    }
    
}
