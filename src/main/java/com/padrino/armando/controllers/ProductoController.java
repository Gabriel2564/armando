package com.padrino.armando.controllers;

import com.padrino.armando.dtos.ProductoDTO;
import com.padrino.armando.services.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de productos
 * Expone endpoints para operaciones CRUD y consultas sobre productos
 *
 * @author Tu Nombre
 * @version 1.0
 */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Permitir CORS - Ajustar en producción con orígenes específicos
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Crear un nuevo producto
     *
     * @param productoDTO datos del producto a crear
     * @return producto creado con código 201
     */
    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        log.info("POST /api/productos - Crear producto con código: {}", productoDTO.getCodigo());
        ProductoDTO nuevoProducto = productoService.crearProducto(productoDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    /**
     * Obtener todos los productos
     *
     * @return lista de todos los productos
     */
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodosLosProductos() {
        log.info("GET /api/productos - Obtener todos los productos");
        List<ProductoDTO> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtener producto por ID
     *
     * @param id ID del producto
     * @return producto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        log.info("GET /api/productos/{} - Obtener producto por ID", id);
        ProductoDTO producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(producto);
    }

    /**
     * Obtener producto por código
     *
     * @param codigo código único del producto
     * @return producto encontrado
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorCodigo(@PathVariable String codigo) {
        log.info("GET /api/productos/codigo/{} - Obtener producto por código", codigo);
        ProductoDTO producto = productoService.obtenerProductoPorCodigo(codigo);
        return ResponseEntity.ok(producto);
    }

    /**
     * Actualizar producto existente
     *
     * @param id ID del producto a actualizar
     * @param productoDTO nuevos datos del producto
     * @return producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO productoDTO) {
        log.info("PUT /api/productos/{} - Actualizar producto", id);
        ProductoDTO productoActualizado = productoService.actualizarProducto(id, productoDTO);
        return ResponseEntity.ok(productoActualizado);
    }

    /**
     * Eliminar producto
     *
     * @param id ID del producto a eliminar
     * @return mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarProducto(@PathVariable Long id) {
        log.info("DELETE /api/productos/{} - Eliminar producto", id);
        productoService.eliminarProducto(id);
        return ResponseEntity.ok(Map.of("message", "Producto eliminado correctamente"));
    }

    /**
     * Buscar productos por término de búsqueda
     *
     * @param q término a buscar en código, nombre o categoría
     * @return lista de productos que coinciden
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarProductos(@RequestParam String q) {
        log.info("GET /api/productos/buscar?q={} - Buscar productos", q);
        List<ProductoDTO> productos = productoService.buscarProductos(q);
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtener productos por categoría
     *
     * @param categoria categoría a filtrar
     * @return lista de productos de la categoría
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosPorCategoria(@PathVariable String categoria) {
        log.info("GET /api/productos/categoria/{} - Obtener productos por categoría", categoria);
        List<ProductoDTO> productos = productoService.obtenerProductosPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtener productos con stock bajo
     *
     * @return lista de productos con stock actual <= stock mínimo
     */
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosConStockBajo() {
        log.info("GET /api/productos/stock-bajo - Obtener productos con stock bajo");
        List<ProductoDTO> productos = productoService.obtenerProductosConStockBajo();
        return ResponseEntity.ok(productos);
    }

    /**
     * Actualizar stock de un producto
     *
     * @param id ID del producto
     * @param request mapa con la nueva cantidad
     * @return producto actualizado
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoDTO> actualizarStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        log.info("PATCH /api/productos/{}/stock - Actualizar stock", id);
        Integer cantidad = request.get("cantidad");
        ProductoDTO producto = productoService.actualizarStock(id, cantidad);
        return ResponseEntity.ok(producto);
    }

    /**
     * Incrementar stock (entrada de inventario)
     *
     * @param id ID del producto
     * @param request mapa con la cantidad a incrementar
     * @return producto actualizado
     */
    @PostMapping("/{id}/incrementar-stock")
    public ResponseEntity<ProductoDTO> incrementarStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        log.info("POST /api/productos/{}/incrementar-stock - Incrementar stock", id);
        Integer cantidad = request.get("cantidad");
        ProductoDTO producto = productoService.incrementarStock(id, cantidad);
        return ResponseEntity.ok(producto);
    }

    /**
     * Decrementar stock (salida de inventario)
     *
     * @param id ID del producto
     * @param request mapa con la cantidad a decrementar
     * @return producto actualizado
     */
    @PostMapping("/{id}/decrementar-stock")
    public ResponseEntity<ProductoDTO> decrementarStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        log.info("POST /api/productos/{}/decrementar-stock - Decrementar stock", id);
        Integer cantidad = request.get("cantidad");
        ProductoDTO producto = productoService.decrementarStock(id, cantidad);
        return ResponseEntity.ok(producto);
    }

    /**
     * Obtener todas las categorías disponibles
     *
     * @return lista de categorías únicas
     */
    @GetMapping("/categorias/todas")
    public ResponseEntity<List<String>> obtenerTodasLasCategorias() {
        log.info("GET /api/productos/categorias/todas - Obtener categorías");
        List<String> categorias = productoService.obtenerTodasLasCategorias();
        return ResponseEntity.ok(categorias);
    }

    /**
     * Obtener todos los proveedores disponibles
     *
     * @return lista de proveedores únicos
     */
    @GetMapping("/proveedores/todos")
    public ResponseEntity<List<String>> obtenerTodosLosProveedores() {
        log.info("GET /api/productos/proveedores/todos - Obtener proveedores");
        List<String> proveedores = productoService.obtenerTodosLosProveedores();
        return ResponseEntity.ok(proveedores);
    }

    /**
     * Obtener estadísticas generales del inventario
     *
     * @return mapa con estadísticas: total productos, stock bajo, categorías, proveedores, valor total
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        log.info("GET /api/productos/estadisticas - Obtener estadísticas");
        Map<String, Object> estadisticas = productoService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }
}
