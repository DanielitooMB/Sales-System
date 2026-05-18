/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.aaron.controller;

import java.util.List;
import mx.itson.aaron.dao.UserDao;
import mx.itson.aaron.model.User;

/**
 *
 * @author Daniel
 */
public class UserController {
    
    private final UserDao userDAO;
    private User usuarioActual;
    
    public UserController() {
        this.userDAO = new UserDao();
        this.usuarioActual = null;
    }
    
    //Autenticar LOGIN
    
    public boolean login(String email, String contraseña) {
        //Validar datos
        if (email == null || email.trim().isEmpty() || contraseña == null || contraseña.trim().isEmpty()) {
            return false;
        }
        
        User usuario = userDAO.buscarPorEmail(email.trim());
        
        //Si no existe, está inactivo o la contraseña no coincide = false
        if (usuario == null || !usuario.isIsActive() || !usuario.getPasswordHash().equals(contraseña)) {
            return false;
        }
        
        //Login exitoso, se guarda la sesión
        this.usuarioActual = usuario;
        return true;
    }
    
    //Registro CREAR CUENTA
    
    public boolean registrarUsuario(String nombre, String email, String contraseña) {
        if (nombre == null || nombre.trim().isEmpty() || email == null || email.trim().isEmpty() || contraseña == null || contraseña.trim().isEmpty()) {
            return false;
        }
        
        //Si el correo ya existe
        if (userDAO.existeEmail(email.trim())) {
            return false;
        }
        
        //Nuevo User por defecto (nombre, email, passwordHash, isActive, rol)
        User nuevoUsuario = new User(
            nombre.trim(),
            email.trim(),
            contraseña, 
            true,
            "vendedor"
        );
        
        return userDAO.insertar(nuevoUsuario);
    }
    
    //control se Sesión
    
    public User getUsuarioActual() {
        return usuarioActual;
    }
    
    public boolean isLogeado() {
        return usuarioActual != null;
    }
    
    public void logout() {
        this.usuarioActual = null;
    }
    
    //Consultas Directas
    
    public List<User> obtenerTodos() {
        return userDAO.obtenerTodos();
    }
    
    public List<User> obtenerActivos() {
        return userDAO.obtenerActivos();
    }
    
    public User obtenerPorId(int id) {
        return userDAO.buscarPorId(id);
    }
    
}
