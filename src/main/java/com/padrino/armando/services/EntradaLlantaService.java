package com.padrino.armando.services;

import com.padrino.armando.dtos.entrada.DetalleEntradaRequestDTO;
import com.padrino.armando.dtos.entrada.EntradaLlantaRequestDTO;
import com.padrino.armando.dtos.entrada.EntradaResponseDTO;
import com.padrino.armando.entities.DetalleEntradaLlanta;
import com.padrino.armando.entities.EntradaLlanta;
import com.padrino.armando.entities.Llanta;
import com.padrino.armando.enums.Moneda;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.mappers.EntradaLlantaMapper;
import com.padrino.armando.repositories.EntradaLlantaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntradaLlantaService {

    private final EntradaLlantaRepository entradaLlantaRepository;
    private final LlantaService llantaService;
    private final EntradaLlantaMapper entradaLlantaMapper;

    @Transactional(readOnly = true)
    public List<EntradaResponseDTO> listarTodas() {
        return entradaLlantaRepository.findAllByOrderByFechaDesc().stream()
                .map(entradaLlantaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EntradaResponseDTO obtenerPorId(Long id) {
        EntradaLlanta entrada = entradaLlantaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrada de llanta no encontrada con ID: " + id));
        return entradaLlantaMapper.toResponseDTO(entrada);
    }

    @Transactional
    public EntradaResponseDTO registrar(EntradaLlantaRequestDTO dto) {
        EntradaLlanta entrada = EntradaLlanta.builder()
                .fecha(dto.getFecha())
                .ruc(dto.getRuc())
                .proveedor(dto.getProveedor())
                .factura(dto.getFactura())
                .formaPago(dto.getFormaPago())
                .detalles(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleEntradaRequestDTO detalleDTO : dto.getDetalles()) {
            Llanta llanta = llantaService.findLlantaOrThrow(detalleDTO.getProductoId());

            BigDecimal precioTotal = detalleDTO.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));

            DetalleEntradaLlanta detalle = DetalleEntradaLlanta.builder()
                    .entradaLlanta(entrada)
                    .llanta(llanta)
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(detalleDTO.getPrecioUnitario())
                    .precioTotal(precioTotal)
                    .build();

            // === Calcular costo en soles ===
            if (detalleDTO.getMoneda() != null && detalleDTO.getCostoUnitario() != null) {
                detalle.setMoneda(detalleDTO.getMoneda());
                detalle.setCostoUnitario(detalleDTO.getCostoUnitario());
                detalle.setTipoCambio(detalleDTO.getTipoCambio());

                if (detalleDTO.getMoneda() == Moneda.DOL && detalleDTO.getTipoCambio() != null) {
                    // Dólares: convertir a soles
                    BigDecimal costoSoles = detalleDTO.getCostoUnitario()
                            .multiply(detalleDTO.getTipoCambio())
                            .setScale(2, RoundingMode.HALF_UP);
                    detalle.setCostoUnitarioSoles(costoSoles);
                    detalle.setCostoTotalSoles(costoSoles.multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));
                } else {
                    // Soles: el costo ya está en soles
                    detalle.setCostoUnitarioSoles(detalleDTO.getCostoUnitario());
                    detalle.setCostoTotalSoles(detalleDTO.getCostoUnitario()
                            .multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));
                }
            }

            entrada.getDetalles().add(detalle);
            total = total.add(precioTotal);
        }

        entrada.setTotal(total);
        entrada = entradaLlantaRepository.save(entrada);
        return entradaLlantaMapper.toResponseDTO(entrada);
    }

    @Transactional
    public void eliminar(Long id) {
        EntradaLlanta entrada = entradaLlantaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrada de llanta no encontrada con ID: " + id));
        entradaLlantaRepository.delete(entrada);
    }
}