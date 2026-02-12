package com.padrino.armando.services;

import com.padrino.armando.dtos.entrada.DetalleEntradaRequestDTO;
import com.padrino.armando.dtos.entrada.EntradaResponseDTO;
import com.padrino.armando.dtos.entrada.EntradaServicioRequestDTO;
import com.padrino.armando.entities.DetalleEntradaServicio;
import com.padrino.armando.entities.EntradaServicio;
import com.padrino.armando.entities.Servicio;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.mappers.EntradaServicioMapper;
import com.padrino.armando.repositories.EntradaServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntradaServicioService {

    private final EntradaServicioRepository entradaServicioRepository;
    private final ServicioService servicioService;
    private final EntradaServicioMapper entradaServicioMapper;

    @Transactional(readOnly = true)
    public List<EntradaResponseDTO> listarTodas() {
        return entradaServicioRepository.findAllByOrderByFechaDesc().stream()
                .map(entradaServicioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EntradaResponseDTO obtenerPorId(Long id) {
        EntradaServicio entrada = entradaServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrada de servicio no encontrada con ID: " + id));
        return entradaServicioMapper.toResponseDTO(entrada);
    }

    @Transactional
    public EntradaResponseDTO registrar(EntradaServicioRequestDTO dto) {
        EntradaServicio entrada = EntradaServicio.builder()
                .fecha(dto.getFecha())
                .ruc(dto.getRuc())
                .proveedor(dto.getProveedor())
                .factura(dto.getFactura())
                .formaPago(dto.getFormaPago())
                .detalles(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleEntradaRequestDTO detalleDTO : dto.getDetalles()) {
            Servicio servicio = servicioService.findServicioOrThrow(detalleDTO.getProductoId());

            BigDecimal precioTotal = detalleDTO.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));

            DetalleEntradaServicio detalle = DetalleEntradaServicio.builder()
                    .entradaServicio(entrada)
                    .servicio(servicio)
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(detalleDTO.getPrecioUnitario())
                    .precioTotal(precioTotal)
                    .build();

            entrada.getDetalles().add(detalle);
            total = total.add(precioTotal);
        }

        entrada.setTotal(total);
        entrada = entradaServicioRepository.save(entrada);
        return entradaServicioMapper.toResponseDTO(entrada);
    }

    @Transactional
    public void eliminar(Long id) {
        EntradaServicio entrada = entradaServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrada de servicio no encontrada con ID: " + id));
        entradaServicioRepository.delete(entrada);
    }
}