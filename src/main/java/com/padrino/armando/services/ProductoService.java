package com.padrino.armando.services;

import com.padrino.armando.dtos.ProductoDTO;
import com.padrino.armando.entities.Producto;
import com.padrino.armando.exceptions.DuplicateResourceException;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.repositories.LoteRepository;
import com.padrino.armando.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;

    public List<ProductoDTO> obtenerTodos() {
        log.info("Obteniendo todos los productos");
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO obtenerPorId(Long id) {
        log.info("Obteniendo producto por ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        return convertirADTO(producto);
    }

    public ProductoDTO obtenerPorCodigo(String codigo) {
        log.info("Obteniendo producto por código: {}", codigo);
        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "codigo", codigo));
        return convertirADTO(producto);
    }

    public List<ProductoDTO> obtenerPorTipo(String tipo) {
        log.info("Obteniendo productos por tipo: {}", tipo);
        return productoRepository.findByTipo(tipo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<String> obtenerTipos() {
        log.info("Obteniendo tipos de productos");
        return productoRepository.findAllTipos();
    }

    public List<ProductoDTO> buscar(String termino) {
        log.info("Buscando productos con término: {}", termino);
        return productoRepository.buscarPorCodigoONombre(termino).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO crear(ProductoDTO dto) {
        log.info("Creando nuevo producto: {}", dto.getCodigo());

        // Validar que no exista el código
        if (productoRepository.findByCodigo(dto.getCodigo()).isPresent()) {
            throw new DuplicateResourceException("Producto", "codigo", dto.getCodigo());
        }

        Producto producto = convertirAEntidad(dto);
        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado exitosamente: {}", guardado.getCodigo());

        return convertirADTO(guardado);
    }

    private ProductoDTO convertirADTO(Producto producto) {
        // Calcular stock total de lotes activos
        Integer stockTotal = loteRepository.findLotesActivosByProducto(producto.getId())
                .stream()
                .mapToInt(lote -> lote.getStockActual())
                .sum();

        return ProductoDTO.builder()
                .id(producto.getId())
                .codigo(producto.getCodigo())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .tipo(producto.getTipo())
                .precioVenta(producto.getPrecioVenta())
                .fechaRegistro(producto.getFechaRegistro())
                .stockTotalActivo(stockTotal)
                .build();
    }

    private Producto convertirAEntidad(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setTipo(dto.getTipo());
        producto.setPrecioVenta(dto.getPrecioVenta());
        return producto;
    }
}