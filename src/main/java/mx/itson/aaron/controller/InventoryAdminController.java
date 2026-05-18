/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.aaron.controller;

import java.util.List;
import mx.itson.aaron.dao.CategoryDao;
import mx.itson.aaron.dao.ProductDao;
import mx.itson.aaron.model.Category;
import mx.itson.aaron.model.Product;

/**
 *
 * @author Daniel
 */
public class InventoryAdminController {
    
    private final ProductController productController;
    private final CategoryDao categoryDao;
    private final ProductDao productDao;
    
    public InventoryAdminController() {
        this.productController = new ProductController();
        this.categoryDao = new CategoryDao();
        this.productDao = new ProductDao();
    }
    
    //Obtener todos los datos para llenar tabla de inventario
    public Object[][] getAllProductsForTable() {
        try {
            Object[][] data = productController.getDataParaTabla();
            
            if (data == null || data.length == 0) {
                System.out.println("No hay productos disponibles");
                return new Object[0][ProductController.COLUMNAS_INVENTARIO.length];
            }
            
            return data;
        } catch (Exception e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
            return new Object[0][ProductController.COLUMNAS_INVENTARIO.length];
        }
    }
    
    //Obtener columnas para la tabla
    public String[] getTableColumns() {
        return ProductController.COLUMNAS_INVENTARIO;
    }
    
    //Obtener todas las categorías para ComboBox
    public List<Category> getAllCategories() {
        try {
            List<Category> categorias = categoryDao.obtenerTodas();
            
            if (categorias == null || categorias.isEmpty()) {
                System.out.println("No hay categorías disponibles");
                return null;
            }
            
            return categorias;
        } catch (Exception e) {
            System.err.println("Error al obtener categorías: " + e.getMessage());
            return null;
        }
    }
    
    //Obtener producto por ID
    public Product getProductById(int id) {
        try {
            return productDao.buscarPorId(id);
        } catch (Exception e) {
            System.err.println("Error al obtener producto: " + e.getMessage());
            return null;
        }
    }
    
    //Crear nuevo producto
    public String createProduct(String nombre, String descripcion, String categoria,
                               String precio, String precioOferta, String stock) {
        try {
            //*Validar campos
            if (nombre == null || nombre.trim().isEmpty()) {
                return "El nombre del producto es obligatorio";
            }
            
            if (precio == null || precio.trim().isEmpty()) {
                return "El precio es obligatorio";
            }
            
            if (stock == null || stock.trim().isEmpty()) {
                return "El stock es obligatorio";
            }
            
            //Usar ProductController para crear
            ProductController.SaveResult result = productController.agregarProducto(
                nombre, descripcion, categoria, precio, precioOferta, stock
            );
            
            switch (result) {
                case OK:
                    System.out.println("Producto creado: " + nombre);
                    return null; //*Sin error
                case NOMBRE_DUPLICADO:
                    return "El nombre del producto ya existe";
                case PRECIO_INVALIDO:
                    return "El precio debe ser un número válido mayor a 0";
                case STOCK_INVALIDO:
                    return "El stock debe ser un número válido mayor o igual a 0";
                case NOMBRE_VACIO:
                    return "El nombre no puede estar vacío";
                default:
                    return "Error al crear el producto";
            }
            
        } catch (Exception e) {
            System.err.println("Error en createProduct: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
    
    //Actualizar producto existente
    public String updateProduct(int id, String nombre, String descripcion, String categoria,
                               String precio, String precioOferta, String stock) {
        try {
            //*Validar campos
            if (nombre == null || nombre.trim().isEmpty()) {
                return "El nombre del producto es obligatorio";
            }
            
            if (precio == null || precio.trim().isEmpty()) {
                return "El precio es obligatorio";
            }
            
            if (stock == null || stock.trim().isEmpty()) {
                return "El stock es obligatorio";
            }
            
            //Usar ProductController para actualizar
            ProductController.SaveResult result = productController.actualizarProducto(
                id, nombre, descripcion, categoria, precio, precioOferta, stock
            );
            
            switch (result) {
                case OK:
                    System.out.println("Producto actualizado: " + nombre);
                    return null; //*Sin error
                case NOMBRE_DUPLICADO:
                    return "El nombre del producto ya existe en otro producto";
                case PRECIO_INVALIDO:
                    return "El precio debe ser un número válido mayor a 0";
                case STOCK_INVALIDO:
                    return "El stock debe ser un número válido mayor o igual a 0";
                case NOMBRE_VACIO:
                    return "El nombre no puede estar vacío";
                default:
                    return "Error al actualizar el producto";
            }
            
        } catch (Exception e) {
            System.err.println("Error en updateProduct: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
    
    //Eliminar producto
    public String deleteProduct(int id) {
        try {
            Product product = productDao.buscarPorId(id);
            
            if (product == null) {
                return "El producto no existe";
            }
            
            if (productController.eliminarProducto(id)) {
                System.out.println("Producto eliminado: " + product.getNombre());
                return null; //*Sin error
            } else {
                return "Error al eliminar el producto";
            }
            
        } catch (Exception e) {
            System.err.println("Error en deleteProduct: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
    
    //Buscar producto por nombre
    public Product searchProductByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return null;
            }
            
            return productDao.buscarPorNombre(name.trim());
        } catch (Exception e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
            return null;
        }
    }
    
}
