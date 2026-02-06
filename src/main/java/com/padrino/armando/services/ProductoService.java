package com.padrino.armando.services;

import com.padrino.armando.dtos.ProductoDTO;
import com.padrino.armando.entities.Producto;
import com.padrino.armando.exceptions.DuplicateResourceException;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio de Producto
 * Contiene la lógica de negocio para la gestión de productos
 *
 * @author Tu Nombre
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        log.info("Creando producto con código: {}", productoDTO.getCodigo());

        // Verificar si ya existe un producto con ese código
        if (productoRepository.existsByCodigo(productoDTO.getCodigo())) {
            log.error("Ya existe un producto con el código: {}", productoDTO.getCodigo());
            throw new DuplicateResourceException("Producto", "codigo", productoDTO.getCodigo());
        }

        Producto producto = convertirDTOAEntidad(productoDTO);
        Producto productoGuardado = productoRepository.save(producto);

        log.info("Producto creado exitosamente con ID: {}", productoGuardado.getId());
        return convertirEntidadADTO(productoGuardado);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerTodosLosProductos() {
        log.info("Obteniendo todos los productos");
        return productoRepository.findAll().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtenerProductoPorId(Long id) {
        log.info("Obteniendo producto por ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        return convertirEntidadADTO(producto);
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtenerProductoPorCodigo(String codigo) {
        log.info("Obteniendo producto por código: {}", codigo);
        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "codigo", codigo));

        return convertirEntidadADTO(producto);
    }

    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        log.info("Actualizando producto con ID: {}", id);

        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        // Verificar si el código está siendo cambiado y si ya existe
        if (!productoExistente.getCodigo().equals(productoDTO.getCodigo())) {
            if (productoRepository.existsByCodigo(productoDTO.getCodigo())) {
                log.error("Ya existe un producto con el código: {}", productoDTO.getCodigo());
                throw new DuplicateResourceException("Producto", "codigo", productoDTO.getCodigo());
            }
        }

        actualizarEntidadDesdeDTO(productoDTO, productoExistente);
        Producto productoActualizado = productoRepository.save(productoExistente);

        log.info("Producto actualizado exitosamente con ID: {}", id);
        return convertirEntidadADTO(productoActualizado);
    }

    public void eliminarProducto(Long id) {
        log.info("Eliminando producto con ID: {}", id);

        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto", "id", id);
        }

        productoRepository.deleteById(id);
        log.info("Producto eliminado exitosamente con ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarProductos(String searchTerm) {
        log.info("Buscando productos con término: {}", searchTerm);
        return productoRepository.buscarProductos(searchTerm).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductosPorCategoria(String categoria) {
        log.info("Obteniendo productos por categoría: {}", categoria);
        return productoRepository.findByCategoria(categoria).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductosConStockBajo() {
        log.info("Obteniendo productos con stock bajo");
        return productoRepository.findProductosConStockBajo().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO actualizarStock(Long id, Integer cantidad) {
        log.info("Actualizando stock del producto ID: {} a cantidad: {}", id, cantidad);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        producto.setStockActual(cantidad);
        Producto productoActualizado = productoRepository.save(producto);

        log.info("Stock actualizado exitosamente");
        return convertirEntidadADTO(productoActualizado);
    }

    public ProductoDTO incrementarStock(Long id, Integer cantidad) {
        log.info("Incrementando stock del producto ID: {} en: {}", id, cantidad);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        producto.setStockActual(producto.getStockActual() + cantidad);
        Producto productoActualizado = productoRepository.save(producto);

        log.info("Stock incrementado exitosamente. Nuevo stock: {}", productoActualizado.getStockActual());
        return convertirEntidadADTO(productoActualizado);
    }

    public ProductoDTO decrementarStock(Long id, Integer cantidad) {
        log.info("Decrementando stock del producto ID: {} en: {}", id, cantidad);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        int nuevoStock = producto.getStockActual() - cantidad;
        if (nuevoStock < 0) {
            log.error("El stock resultante sería negativo. Stock actual: {}, cantidad a decrementar: {}",
                    producto.getStockActual(), cantidad);
            throw new IllegalArgumentException("El stock no puede ser negativo. Stock actual: " +
                    producto.getStockActual());
        }

        producto.setStockActual(nuevoStock);
        Producto productoActualizado = productoRepository.save(producto);

        log.info("Stock decrementado exitosamente. Nuevo stock: {}", productoActualizado.getStockActual());
        return convertirEntidadADTO(productoActualizado);
    }

    @Transactional(readOnly = true)
    public List<String> obtenerTodasLasCategorias() {
        log.info("Obteniendo todas las categorías");
        return productoRepository.findAllCategorias();
    }

    @Transactional(readOnly = true)
    public List<String> obtenerTodosLosProveedores() {
        log.info("Obteniendo todos los proveedores");
        return productoRepository.findAllProveedores();
    }

    @Transactional(readOnly = true)
    public Long contarProductosConStockBajo() {
        log.info("Contando productos con stock bajo");
        return productoRepository.contarProductosConStockBajo();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticas() {
        log.info("Obteniendo estadísticas del inventario");

        List<Producto> todosLosProductos = productoRepository.findAll();

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalProductos", todosLosProductos.size());
        estadisticas.put("productosConStockBajo", productoRepository.contarProductosConStockBajo());
        estadisticas.put("totalCategorias", productoRepository.findAllCategorias().size());
        estadisticas.put("totalProveedores", productoRepository.findAllProveedores().size());

        BigDecimal valorTotalInventario = todosLosProductos.stream()
                .map(p -> {
                    BigDecimal precio = p.getPrecioCompra() != null ? p.getPrecioCompra() : BigDecimal.ZERO;
                    Integer stock = p.getStockActual() != null ? p.getStockActual() : 0;
                    return precio.multiply(new BigDecimal(stock));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        estadisticas.put("valorTotalInventario", valorTotalInventario);

        log.info("Estadísticas calculadas: {} productos, {} con stock bajo",
                estadisticas.get("totalProductos"), estadisticas.get("productosConStockBajo"));

        return estadisticas;
    }

    // Métodos privados auxiliares para conversión

    private ProductoDTO convertirEntidadADTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .codigo(producto.getCodigo())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .categoria(producto.getCategoria())
                .precioCompra(producto.getPrecioCompra())
                .precioVenta(producto.getPrecioVenta())
                .stockActual(producto.getStockActual())
                .stockMinimo(producto.getStockMinimo())
                .proveedor(producto.getProveedor())
                .fechaRegistro(producto.getFechaRegistro())
                .ubicacion(producto.getUbicacion())
                .stockBajo(producto.isStockBajo())
                .margenGanancia(producto.getMargenGanancia())
                .porcentajeMargen(producto.getPorcentajeMargen())
                .build();
    }

    private Producto convertirDTOAEntidad(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setStockActual(dto.getStockActual());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setProveedor(dto.getProveedor());
        producto.setFechaRegistro(dto.getFechaRegistro());
        producto.setUbicacion(dto.getUbicacion());
        return producto;
    }

    private void actualizarEntidadDesdeDTO(ProductoDTO dto, Producto producto) {
        if (dto.getCodigo() != null) {
            producto.setCodigo(dto.getCodigo());
        }
        if (dto.getNombre() != null) {
            producto.setNombre(dto.getNombre());
        }
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecioCompra(dto.getPrecioCompra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        if (dto.getStockActual() != null) {
            producto.setStockActual(dto.getStockActual());
        }
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setProveedor(dto.getProveedor());
        producto.setUbicacion(dto.getUbicacion());
    }
}
