/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.aaron.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import mx.itson.aaron.dao.OrderDao;
import mx.itson.aaron.dao.ProductDao;
import mx.itson.aaron.model.Client;
import mx.itson.aaron.model.Order;
import mx.itson.aaron.model.Product;
import mx.itson.aaron.model.ProductOrder;

/**
 *
 * @author Daniel
 */
public class PointSaleController {
 
    //Representa una línea del carrito
    public static class CartItem {
        private final int productoId;
        private final String nombre;
        private final String categoria;
        private final int cantidad;
        private final BigDecimal precioUnitario;
 
        public CartItem(int productoId, String nombre, String categoria, int cantidad, BigDecimal precioUnitario) {
            this.productoId     = productoId;
            this.nombre         = nombre;
            this.categoria      = categoria;
            this.cantidad       = cantidad;
            this.precioUnitario = precioUnitario;
        }
 
        public int    getProductoId()    { return productoId; }
        public String getNombre()        { return nombre; }
        public String getCategoria()     { return categoria; }
        public int    getCantidad()      { return cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public BigDecimal getTotal()     { return precioUnitario.multiply(BigDecimal.valueOf(cantidad)); }
    }
 
    //Resultado de agregar un item al carrito
    public enum AddResult {
        OK,
        PRODUCTO_NO_ENCONTRADO,
        STOCK_INSUFICIENTE,
        CANTIDAD_INVALIDA
    }
 
    //Estado
    private final ProductDao   productDAO;
    private final OrderDao     orderDAO;
    private final ClientController clientController;
 
    private final List<CartItem> carrito = new ArrayList<>();
    private Client clienteActual;
    private int    usuarioId;
 
    public PointSaleController(int usuarioId) {
        this.productDAO        = new ProductDao();
        this.orderDAO          = new OrderDao();
        this.clientController  = new ClientController();
        this.usuarioId         = usuarioId;
    }
 
    //Cliente
    //Carga el cliente a partir del ID ingresado en ClientTfld.
    public Client cargarCliente(String idTexto) {
        try {
            int id = Integer.parseInt(idTexto.trim());
            clienteActual = clientController.buscarPorId(id);
            return clienteActual;
        } catch (NumberFormatException e) {
            clienteActual = null;
            return null;
        }
    }
 
    public Client getClienteActual() { return clienteActual; }
 
    //Categorías
    //Devuelve los nombres de categoría tal como están en la BD (para CategoryBox)
    public String[] obtenerCategorias() {
        return new String[]{"Carnes frias", "Pescados", "Verduras", "Frutas", "Lacteos", "Bebidas", "Snacks"};
    }
 
    //Devuelve los productos de una categoría para sugerencias/validación
    public List<Product> obtenerProductosPorCategoria(String categoria) {
        return productDAO.obtenerPorCategoria(categoria);
    }
 
    //Carrito
    //Agrega un producto al carrito.
    //El precio que se guarda es el que viene del campo PriceTfld (precio ingresado manualmente), pero se valida contra el stock real.
    public AddResult agregarAlCarrito(String nombreProducto, String categoriaSeleccionada,
                                      String cantidadTexto, String precioTexto) {
        //Validar cantidad
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTexto.trim());
            if (cantidad <= 0) return AddResult.CANTIDAD_INVALIDA;
        } catch (NumberFormatException e) {
            return AddResult.CANTIDAD_INVALIDA;
        }
 
        //Validar precio
        BigDecimal precio;
        try {
            precio = new BigDecimal(precioTexto.trim().replace(",", ""));
            if (precio.compareTo(BigDecimal.ZERO) <= 0) return AddResult.CANTIDAD_INVALIDA;
        } catch (NumberFormatException e) {
            return AddResult.CANTIDAD_INVALIDA;
        }
 
        //Buscar producto por nombre
        Product producto = productDAO.buscarPorNombre(nombreProducto.trim());
        if (producto == null) return AddResult.PRODUCTO_NO_ENCONTRADO;
 
        //Verificar stock (incluyendo lo ya en el carrito)
        int cantidadEnCarrito = carrito.stream()
                .filter(i -> i.getProductoId() == producto.getId())
                .mapToInt(CartItem::getCantidad)
                .sum();
 
        if (producto.getStock() < cantidadEnCarrito + cantidad) {
            return AddResult.STOCK_INSUFICIENTE;
        }
 
        //Agregar al carrito
        carrito.add(new CartItem(
            producto.getId(),
            producto.getNombre(),
            categoriaSeleccionada,
            cantidad,
            precio
        ));
 
        return AddResult.OK;
    }
 
    //Elimina el item en la fila
    public void eliminarDelCarrito(int fila) {
        if (fila >= 0 && fila < carrito.size()) {
            carrito.remove(fila);
        }
    }
 
    //Devuelve el carrito ordenado de menor a mayor por total de línea
    public List<CartItem> getCarritoOrdenado() {
        return carrito.stream()
                .sorted((a, b) -> a.getTotal().compareTo(b.getTotal()))
                .collect(java.util.stream.Collectors.toList());
    }
 
    //Calcula el total de toda la compra
    public BigDecimal calcularTotal() {
        return carrito.stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
 
    //Devuelve true si el carrito no tiene items
    public boolean carritoVacio() { return carrito.isEmpty(); }
 
    //Limpia el carrito (después de finalizar)
    public void limpiarCarrito() { carrito.clear(); }
 
    //Finalizar compra
    //Guarda la orden en la BD y descuenta stock.
    public int finalizarCompra() {
        if (clienteActual == null || carritoVacio()) return -1;
 
        Order orden = new Order(
            clienteActual.getId(),
            usuarioId,
            calcularTotal(),
            true
        );
 
        //Convertir carrito a ProductOrder
        List<ProductOrder> items = new ArrayList<>();
        for (CartItem item : carrito) {
            items.add(new ProductOrder(0, item.getProductoId(), item.getCantidad(), item.getPrecioUnitario()));
        }
 
        int ordenId = orderDAO.insertarOrdenCompleta(orden, items);
        if (ordenId > 0) {
            limpiarCarrito();
            clienteActual = null;
        }
        return ordenId;
    }
 
    //Datos para la tabla
    //Convierte el carrito ordenado a un Object[][] listo para DefaultTableModel.
    //Columnas: Producto | Categoría | Cantidad | Precio Unit. | Total
    public Object[][] getDataParaTabla() {
        List<CartItem> ordenado = getCarritoOrdenado();
        Object[][] data = new Object[ordenado.size()][5];
        for (int i = 0; i < ordenado.size(); i++) {
            CartItem item = ordenado.get(i);
            data[i][0] = item.getNombre();
            data[i][1] = item.getCategoria();
            data[i][2] = item.getCantidad();
            data[i][3] = "$" + item.getPrecioUnitario();
            data[i][4] = "$" + item.getTotal();
        }
        return data;
    }
 
    public static final String[] COLUMNAS_TABLA = {"Producto", "Categoría", "Cantidad", "Precio Unit.", "Total"};
    
}
