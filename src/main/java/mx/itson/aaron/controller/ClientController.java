/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.aaron.controller;

import java.util.List;
import mx.itson.aaron.dao.ClientDao;
import mx.itson.aaron.model.Client;

/**
 *
 * @author Daniel
 */
public class ClientController {
    
    private final ClientDao clientDAO;
 
    public ClientController() {
        this.clientDAO = new ClientDao();
    }
 
    // Registrar nuevo cliente
    public boolean registrarCliente(String nombre, String email, String telefono, String direccion) {
        // Validar campos obligatorios
        if (nombre == null || nombre.trim().isEmpty()) return false;
        if (telefono == null || telefono.trim().isEmpty()) return false;
 
        // Teléfono es UNIQUE en la BD
        if (clientDAO.existeTelefono(telefono.trim())) return false;
 
        Client nuevoCliente = new Client(
            nombre.trim(),
            (email != null && !email.trim().isEmpty()) ? email.trim() : null,
            telefono.trim(),
            (direccion != null && !direccion.trim().isEmpty()) ? direccion.trim() : null
        );
 
        return clientDAO.insertar(nuevoCliente);
    }
 
    // Buscar cliente por ID (usado en el PointSale)
    public Client buscarPorId(int id) {
        return clientDAO.buscarPorId(id);
    }
 
    // Buscar cliente por teléfono
    public Client buscarPorTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) return null;
        return clientDAO.buscarPorTelefono(telefono.trim());
    }
 
    // Obtener todos los clientes
    public List<Client> obtenerTodos() {
        return clientDAO.obtenerTodos();
    }
 
    // Verificar si el teléfono ya está registrado
    public boolean telefonoExiste(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) return false;
        return clientDAO.existeTelefono(telefono.trim());
    }
    
}
