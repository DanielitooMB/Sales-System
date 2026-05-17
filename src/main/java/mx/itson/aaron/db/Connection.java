/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.aaron.db;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Daniel
 */
public class Connection {
    
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/db_sistema_ventas";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";
    
    private static java.sql.Connection connection = null;
    
    public static java.sql.Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa");
            }
            
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver no encontrado");
            throw new SQLException("Driver no encontrado", e);
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            throw e;
        }
    }
}
