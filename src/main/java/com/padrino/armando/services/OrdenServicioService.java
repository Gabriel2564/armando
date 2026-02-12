package com.padrino.armando.services;

import com.padrino.armando.dtos.orden.DetalleOrdenRequestDTO;
import com.padrino.armando.dtos.orden.OrdenServicioRequestDTO;
import com.padrino.armando.dtos.orden.OrdenServicioResponseDTO;
import com.padrino.armando.entities.*;
import com.padrino.armando.exceptions.DuplicateResourceException;
import com.padrino.armando.exceptions.InsufficientStockException;
import com.padrino.armando.exceptions.InvalidOperationException;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.mappers.OrdenServicioMapper;
import com.padrino.armando.repositories.LlantaRepository;
import com.padrino.armando.repositories.OrdenServicioRepository;
import com.padrino.armando.repositories.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenServicioService {

    private final OrdenServicioRepository ordenServicioRepository;
    private final LlantaService llantaService;
    private final ServicioService servicioService;
    private final LlantaRepository llantaRepository;
    private final ServicioRepository servicioRepository;
    private final OrdenServicioMapper ordenServicioMapper;

    @Transactional(readOnly = true)
    public List<OrdenServicioResponseDTO> listarTodas() {
        return ordenServicioRepository.findAllByOrderByFechaDesc().stream()
                .map(ordenServicioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrdenServicioResponseDTO obtenerPorId(Long id) {
        OrdenServicio orden = findOrdenOrThrow(id);
        return ordenServicioMapper.toResponseDTO(orden);
    }

    @Transactional(readOnly = true)
    public List<OrdenServicioResponseDTO> buscarPorCliente(String cliente) {
        return ordenServicioRepository.findByClienteContainingIgnoreCase(cliente).stream()
                .map(ordenServicioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrdenServicioResponseDTO> buscarPorPlaca(String placa) {
        return ordenServicioRepository.findByPlacaContainingIgnoreCase(placa).stream()
                .map(ordenServicioMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public OrdenServicioResponseDTO registrar(OrdenServicioRequestDTO dto) {
        if (ordenServicioRepository.existsByNumeroOrden(dto.getNumeroOrden())) {
            throw new DuplicateResourceException("Ya existe una orden con número: " + dto.getNumeroOrden());
        }

        OrdenServicio orden = OrdenServicio.builder()
                .numeroOrden(dto.getNumeroOrden())
                .fecha(dto.getFecha())
                .ruc(dto.getRuc())
                .cliente(dto.getCliente())
                .placa(dto.getPlaca())
                .formaPago(dto.getFormaPago())
                .detalles(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleOrdenRequestDTO detalleDTO : dto.getDetalles()) {
            DetalleOrdenServicio detalle = DetalleOrdenServicio.builder()
                    .ordenServicio(orden)
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(detalleDTO.getPrecioUnitario())
                    .build();

            if (detalleDTO.getLlantaId() != null) {
                Llanta llanta = llantaService.findLlantaOrThrow(detalleDTO.getLlantaId());
                validarStockLlanta(llanta.getId(), detalleDTO.getCantidad());
                detalle.setLlanta(llanta);
            } else if (detalleDTO.getServicioId() != null) {
                Servicio servicio = servicioService.findServicioOrThrow(detalleDTO.getServicioId());
                validarStockServicio(servicio.getId(), detalleDTO.getCantidad());
                detalle.setServicio(servicio);
            } else {
                throw new InvalidOperationException("Cada detalle debe tener un llantaId o servicioId");
            }

            BigDecimal precioTotal = detalleDTO.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));
            detalle.setPrecioTotal(precioTotal);

            orden.getDetalles().add(detalle);
            total = total.add(precioTotal);
        }

        orden.setTotal(total);
        orden = ordenServicioRepository.save(orden);
        return ordenServicioMapper.toResponseDTO(orden);
    }

    @Transactional
    public void eliminar(Long id) {
        OrdenServicio orden = findOrdenOrThrow(id);
        ordenServicioRepository.delete(orden);
    }

    private OrdenServicio findOrdenOrThrow(Long id) {
        return ordenServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de servicio no encontrada con ID: " + id));
    }

    private void validarStockLlanta(Long llantaId, Integer cantidadSolicitada) {
        Integer entradas = llantaRepository.calcularTotalEntradas(llantaId);
        Integer salidas = llantaRepository.calcularTotalSalidas(llantaId);
        int stockActual = entradas - salidas;

        if (stockActual < cantidadSolicitada) {
            throw new InsufficientStockException(
                    "Stock insuficiente de llanta. Disponible: " + stockActual + ", Solicitado: " + cantidadSolicitada);
        }
    }

    private void validarStockServicio(Long servicioId, Integer cantidadSolicitada) {
        Integer entradas = servicioRepository.calcularTotalEntradas(servicioId);
        Integer salidas = servicioRepository.calcularTotalSalidas(servicioId);
        int stockActual = entradas - salidas;

        if (stockActual < cantidadSolicitada) {
            throw new InsufficientStockException(
                    "Stock insuficiente de producto. Disponible: " + stockActual + ", Solicitado: " + cantidadSolicitada);
        }
    }
}