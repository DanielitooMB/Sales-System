/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.aaron.controller;

import java.math.BigDecimal;
import java.util.List;
import mx.itson.aaron.dao.CategoryDao;
import mx.itson.aaron.dao.ProductDao;
import mx.itson.aaron.model.Category;
import mx.itson.aaron.model.Product;

/**
 *
 * @author Daniel
 */
public class ProductController {
    
    private final ProductDao productDAO;
    private final CategoryDao categoryDAO;
 
    // Columnas de la tabla de inventario
    public static final String[] COLUMNAS_INVENTARIO = {
        "ID", "Nombre", "Descripción", "Categoría", "Precio", "Precio Oferta", "Stock"
    };
 
    public ProductController() {
        this.productDAO = new ProductDao();
        this.categoryDAO = new CategoryDao();
    }
 
    // ── Lectura ──────────────────────────────────────────────────────────────
 
    // Obtener todos los productos ordenados por precio (para la tabla)
    public List<Product> obtenerTodos() {
        return productDAO.obtenerOrdenadosPorPrecio();
    }
 
    // Buscar producto por ID
    public Product buscarPorId(int id) {
        return productDAO.buscarPorId(id);
    }
 
    // Obtener todas las categorías (para el JComboBox)
    public List<Category> obtenerCategorias() {
        return categoryDAO.obtenerTodas();
    }
 
    // Convierte la lista de productos a Object[][] para DefaultTableModel
    public Object[][] getDataParaTabla() {
        List<Product> productos = obtenerTodos();
        Object[][] data = new Object[productos.size()][COLUMNAS_INVENTARIO.length];
        for (int i = 0; i < productos.size(); i++) {
            Product p = productos.get(i);
            Category cat = categoryDAO.buscarPorId(p.getCategoriaId());
            String nombreCat = cat != null ? cat.getNombre() : "Sin categoría";
            data[i][0] = p.getId();
            data[i][1] = p.getNombre();
            data[i][2] = p.getDescripcion();
            data[i][3] = nombreCat;
            data[i][4] = "$" + p.getPrecio();
            data[i][5] = p.getPrecioOferta() != null ? "$" + p.getPrecioOferta() : "-";
            data[i][6] = p.getStock();
        }
        return data;
    }
 
    //Creación 
    public enum SaveResult {
        OK,
        NOMBRE_VACIO,
        PRECIO_INVALIDO,
        STOCK_INVALIDO,
        NOMBRE_DUPLICADO,
        ERROR_BD
    }
 
    //Insertar nuevo producto
    public SaveResult agregarProducto(String nombre, String descripcion, String categoriaNombre,
                                      String precioTexto, String precioOfertaTexto, String stockTexto) {
        SaveResult validacion = validarCampos(nombre, precioTexto, precioOfertaTexto, stockTexto, -1);
        if (validacion != SaveResult.OK) return validacion;
 
        if (productDAO.existeNombre(nombre.trim())) return SaveResult.NOMBRE_DUPLICADO;
 
        Category cat = categoryDAO.buscarPorNombre(categoriaNombre);
        if (cat == null) return SaveResult.ERROR_BD;
 
        Product producto = new Product(
            nombre.trim(),
            descripcion.trim(),
            new BigDecimal(precioTexto.trim()),
            (precioOfertaTexto == null || precioOfertaTexto.trim().isEmpty())
                ? null : new BigDecimal(precioOfertaTexto.trim()),
            Integer.parseInt(stockTexto.trim()),
            cat.getId()
        );
 
        return productDAO.insertar(producto) ? SaveResult.OK : SaveResult.ERROR_BD;
    }
 
    //Actualizar producto existente
    public SaveResult actualizarProducto(int id, String nombre, String descripcion, String categoriaNombre,
                                          String precioTexto, String precioOfertaTexto, String stockTexto) {
        SaveResult validacion = validarCampos(nombre, precioTexto, precioOfertaTexto, stockTexto, id);
        if (validacion != SaveResult.OK) return validacion;
 
        // Verificar nombre duplicado en otro producto
        Product existente = productDAO.buscarPorNombre(nombre.trim());
        if (existente != null && existente.getId() != id) return SaveResult.NOMBRE_DUPLICADO;
 
        Category cat = categoryDAO.buscarPorNombre(categoriaNombre);
        if (cat == null) return SaveResult.ERROR_BD;
 
        Product producto = new Product(
            nombre.trim(),
            descripcion.trim(),
            new BigDecimal(precioTexto.trim()),
            (precioOfertaTexto == null || precioOfertaTexto.trim().isEmpty())
                ? null : new BigDecimal(precioOfertaTexto.trim()),
            Integer.parseInt(stockTexto.trim()),
            cat.getId()
        );
        producto.setId(id);
 
        return productDAO.actualizar(producto) ? SaveResult.OK : SaveResult.ERROR_BD;
    }
 
    //Eliminación
 
    public boolean eliminarProducto(int id) {
        return productDAO.eliminar(id);
    }
 
    //Validación interna
 
    private SaveResult validarCampos(String nombre, String precioTexto, String precioOfertaTexto,
                                      String stockTexto, int idActual) {
        if (nombre == null || nombre.trim().isEmpty()) return SaveResult.NOMBRE_VACIO;
 
        try {
            BigDecimal precio = new BigDecimal(precioTexto.trim());
            if (precio.compareTo(BigDecimal.ZERO) <= 0) return SaveResult.PRECIO_INVALIDO;
        } catch (NumberFormatException | NullPointerException e) {
            return SaveResult.PRECIO_INVALIDO;
        }
 
        if (precioOfertaTexto != null && !precioOfertaTexto.trim().isEmpty()) {
            try {
                BigDecimal oferta = new BigDecimal(precioOfertaTexto.trim());
                if (oferta.compareTo(BigDecimal.ZERO) <= 0) return SaveResult.PRECIO_INVALIDO;
            } catch (NumberFormatException e) {
                return SaveResult.PRECIO_INVALIDO;
            }
        }
 
        try {
            int stock = Integer.parseInt(stockTexto.trim());
            if (stock < 0) return SaveResult.STOCK_INVALIDO;
        } catch (NumberFormatException | NullPointerException e) {
            return SaveResult.STOCK_INVALIDO;
        }
 
        return SaveResult.OK;
    }
    
}
