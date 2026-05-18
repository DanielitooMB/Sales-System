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
import mx.itson.aaron.model.Category;

/**
 *
 * @author Daniel
 */
public class CategoryDao {
 
    //Obtener todas las categorías ordenadas por nombre
    public List<Category> obtenerTodas() {
        List<Category> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY nombre ASC";
        try (java.sql.Connection conn = Connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
 
            while (rs.next()) {
                categorias.add(new Category(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }
 
    //Buscar categoría por nombre exacto
    public Category buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM categorias WHERE nombre = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    //Buscar categoría por ID
    public Category buscarPorId(int id) {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        try (java.sql.Connection conn = Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
