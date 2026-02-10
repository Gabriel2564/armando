package com.padrino.armando.services;

import com.padrino.armando.dtos.LoteDTO;
import com.padrino.armando.entities.Lote;
import com.padrino.armando.entities.Producto;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.repositories.LoteRepository;
import com.padrino.armando.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoteService {

    private final LoteRepository loteRepository;
    private final ProductoRepository productoRepository;

    public List<LoteDTO> obtenerTodos() {
        log.info("Obteniendo todos los lotes");
        return loteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<LoteDTO> obtenerActivos() {
        log.info("Obteniendo lotes activos");
        return loteRepository.findAllActivos().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<LoteDTO> obtenerPorTipo(String tipo) {
        log.info("Obteniendo lotes por tipo: {}", tipo);
        return loteRepository.findByTipo(tipo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<LoteDTO> obtenerPorProducto(Long productoId) {
        log.info("Obteniendo lotes del producto: {}", productoId);
        return loteRepository.findByProductoIdOrderByFechaEntradaAsc(productoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public LoteDTO crearNuevoLote(LoteDTO dto) {
        log.info("Creando nuevo lote para producto: {}", dto.getProductoId());

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", dto.getProductoId()));

        Lote lote = new Lote();
        lote.setProducto(producto);
        lote.setStockInicial(dto.getStockInicial());
        lote.setStockActual(dto.getStockInicial());
        lote.setPrecioCompra(dto.getPrecioCompra());
        lote.setFechaEntrada(dto.getFechaEntrada() != null ? dto.getFechaEntrada() : LocalDateTime.now());
        lote.setObservaciones(dto.getObservaciones());

        // Generar número de lote automático
        int numeroSecuencia = loteRepository.findByProductoIdOrderByFechaEntradaAsc(producto.getId()).size() + 1;
        String numeroLote = String.format("LOTE-%s-%d", producto.getCodigo(), numeroSecuencia);
        lote.setNumeroLote(numeroLote);

        Lote guardado = loteRepository.save(lote);
        log.info("Lote creado exitosamente: {}", guardado.getNumeroLote());

        return convertirADTO(guardado);
    }

    public void descontarStock(Long loteId, Integer cantidad) {
        log.info("Descontando {} unidades del lote {}", cantidad, loteId);

        Lote lote = loteRepository.findById(loteId)
                .orElseThrow(() -> new ResourceNotFoundException("Lote", "id", loteId));

        if (lote.getStockActual() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente en el lote: " + lote.getNumeroLote());
        }

        lote.descontarStock(cantidad);
        loteRepository.save(lote);

        log.info("Stock actualizado. Lote: {} - Stock restante: {}", lote.getNumeroLote(), lote.getStockActual());
    }

    private LoteDTO convertirADTO(Lote lote) {
        return LoteDTO.builder()
                .id(lote.getId())
                .productoId(lote.getProducto().getId())
                .productoCodigo(lote.getProducto().getCodigo())
                .productoNombre(lote.getProducto().getNombre())
                .productoTipo(lote.getProducto().getTipo())
                .numeroLote(lote.getNumeroLote())
                .stockInicial(lote.getStockInicial())
                .stockActual(lote.getStockActual())
                .precioCompra(lote.getPrecioCompra())
                .fechaEntrada(lote.getFechaEntrada())
                .fechaTerminacion(lote.getFechaTerminacion())
                .estado(lote.getEstado())
                .observaciones(lote.getObservaciones())
                .stockVendido(lote.getStockVendido())
                .diasActivo(lote.getDiasActivo())
                .build();
    }
}