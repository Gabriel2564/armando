package com.padrino.armando.services;

import com.padrino.armando.dtos.venta.DetalleVentaRequestDTO;
import com.padrino.armando.dtos.venta.VentaLlantaRequestDTO;
import com.padrino.armando.dtos.venta.VentaLlantaResponseDTO;
import com.padrino.armando.entities.DetalleVentaLlanta;
import com.padrino.armando.entities.Llanta;
import com.padrino.armando.entities.VentaLlanta;
import com.padrino.armando.exceptions.DuplicateResourceException;
import com.padrino.armando.exceptions.InsufficientStockException;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.mappers.VentaLlantaMapper;
import com.padrino.armando.repositories.LlantaRepository;
import com.padrino.armando.repositories.VentaLlantaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaLlantaService {

    private final VentaLlantaRepository ventaLlantaRepository;
    private final LlantaService llantaService;
    private final LlantaRepository llantaRepository;
    private final CostoPromedioService costoPromedioService;
    private final VentaLlantaMapper ventaLlantaMapper;

    @Transactional(readOnly = true)
    public List<VentaLlantaResponseDTO> listarTodas() {
        return ventaLlantaRepository.findAllByOrderByFechaDesc().stream()
                .map(ventaLlantaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public VentaLlantaResponseDTO obtenerPorId(Long id) {
        return ventaLlantaMapper.toResponseDTO(findVentaOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<VentaLlantaResponseDTO> buscarPorCliente(String cliente) {
        return ventaLlantaRepository.findByClienteContainingIgnoreCase(cliente).stream()
                .map(ventaLlantaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VentaLlantaResponseDTO> buscarPorPlaca(String placa) {
        return ventaLlantaRepository.findByPlacaContainingIgnoreCase(placa).stream()
                .map(ventaLlantaMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public VentaLlantaResponseDTO registrar(VentaLlantaRequestDTO dto) {
        if (ventaLlantaRepository.existsByNumeroVenta(dto.getNumeroVenta())) {
            throw new DuplicateResourceException("Ya existe una venta con número: " + dto.getNumeroVenta());
        }

        VentaLlanta venta = VentaLlanta.builder()
                .numeroVenta(dto.getNumeroVenta())
                .fecha(dto.getFecha())
                .ruc(dto.getRuc())
                .cliente(dto.getCliente())
                .placa(dto.getPlaca())
                .tipoComprobante(dto.getTipoComprobante())
                .numeroComprobante(dto.getNumeroComprobante())
                .formaPago(dto.getFormaPago())
                .detalles(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVentaRequestDTO detalleDTO : dto.getDetalles()) {
            Llanta llanta = llantaService.findLlantaOrThrow(detalleDTO.getLlantaId());
            validarStock(llanta.getId(), detalleDTO.getCantidad());

            BigDecimal precioTotal = detalleDTO.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalleDTO.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);

            // Calcular costo promedio vigente
            BigDecimal costoPromedio = costoPromedioService.calcularCostoPromedio(llanta.getId());
            BigDecimal costoTotal = costoPromedio
                    .multiply(BigDecimal.valueOf(detalleDTO.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal ganancia = precioTotal.subtract(costoTotal);
            BigDecimal margen = precioTotal.compareTo(BigDecimal.ZERO) > 0
                    ? ganancia.divide(precioTotal, 4, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            DetalleVentaLlanta detalle = DetalleVentaLlanta.builder()
                    .ventaLlanta(venta)
                    .llanta(llanta)
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(detalleDTO.getPrecioUnitario())
                    .precioTotal(precioTotal)
                    .costoUnitario(costoPromedio)
                    .costoTotal(costoTotal)
                    .ganancia(ganancia)
                    .margen(margen)
                    .build();

            venta.getDetalles().add(detalle);
            total = total.add(precioTotal);
        }

        venta.setTotal(total);
        venta = ventaLlantaRepository.save(venta);
        return ventaLlantaMapper.toResponseDTO(venta);
    }

    @Transactional
    public void eliminar(Long id) {
        VentaLlanta venta = findVentaOrThrow(id);
        ventaLlantaRepository.delete(venta);
    }

    private VentaLlanta findVentaOrThrow(Long id) {
        return ventaLlantaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));
    }

    private void validarStock(Long llantaId, Integer cantidadSolicitada) {
        Integer entradas = llantaRepository.calcularTotalEntradas(llantaId);
        Integer salidas = llantaRepository.calcularTotalSalidas(llantaId);
        // También contar las ventas directas
        Integer ventas = ventaLlantaRepository.calcularTotalVentas(llantaId);
        int stockActual = entradas - salidas - ventas;
        if (stockActual < cantidadSolicitada) {
            throw new InsufficientStockException(
                    "Stock insuficiente. Disponible: " + stockActual + ", Solicitado: " + cantidadSolicitada);
        }
    }
}