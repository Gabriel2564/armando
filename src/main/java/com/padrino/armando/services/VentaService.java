package com.padrino.armando.services;

import com.padrino.armando.dtos.DetalleVentaDTO;
import com.padrino.armando.dtos.VentaDTO;
import com.padrino.armando.entities.DetalleVenta;
import com.padrino.armando.entities.Producto;
import com.padrino.armando.entities.Venta;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.repositories.ProductoRepository;
import com.padrino.armando.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public VentaDTO registrarVenta(VentaDTO ventaDTO) {
        log.info("Registrando nueva venta");

        Venta venta = new Venta();
        venta.setCliente(ventaDTO.getCliente());
        venta.setDocumento(ventaDTO.getDocumento());
        venta.setObservaciones(ventaDTO.getObservaciones());

        // Procesar detalles
        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            DetalleVenta detalle = new DetalleVenta();

            // Buscar producto
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", detalleDTO.getProductoId()));

            // Verificar stock disponible (solo si no es servicio)
            if (!detalleDTO.getEsServicio() && producto.getStockActual() < detalleDTO.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto: " + producto.getNombre() +
                                ". Disponible: " + producto.getStockActual()
                );
            }

            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalle.setEsServicio(detalleDTO.getEsServicio());
            detalle.setDescripcion(detalleDTO.getDescripcion());
            detalle.calcularSubtotal();
            detalle.setVenta(venta);

            venta.getDetalles().add(detalle);

            // Actualizar stock (solo si no es servicio)
            if (!detalleDTO.getEsServicio()) {
                producto.setStockActual(producto.getStockActual() - detalleDTO.getCantidad());
                productoRepository.save(producto);
                log.info("Stock actualizado para producto: {} - Nuevo stock: {}",
                        producto.getNombre(), producto.getStockActual());
            }
        }

        // Calcular totales
        venta.calcularTotales();

        // Guardar venta
        Venta ventaGuardada = ventaRepository.save(venta);
        log.info("Venta registrada exitosamente. Número de boleta: {}", ventaGuardada.getNumeroBoleta());

        return convertirEntidadADTO(ventaGuardada);
    }

    @Transactional(readOnly = true)
    public List<VentaDTO> obtenerVentasDelDia() {
        log.info("Obteniendo ventas del día");
        return ventaRepository.findVentasDelDia().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VentaDTO> obtenerVentasPorFecha(LocalDate fecha) {
        log.info("Obteniendo ventas de la fecha: {}", fecha);
        return ventaRepository.findByFechaNativa(fecha).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VentaDTO> obtenerVentasPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Obteniendo ventas entre {} y {}", fechaInicio, fechaFin);
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        return ventaRepository.findByFechaRange(inicio, fin).stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VentaDTO obtenerVentaPorId(Long id) {
        log.info("Obteniendo venta por ID: {}", id);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));
        return convertirEntidadADTO(venta);
    }

    private VentaDTO convertirEntidadADTO(Venta venta) {
        List<DetalleVentaDTO> detallesDTO = venta.getDetalles().stream()
                .map(detalle -> DetalleVentaDTO.builder()
                        .id(detalle.getId())
                        .productoId(detalle.getProducto().getId())
                        .productoNombre(detalle.getProducto().getNombre())
                        .productoCodigo(detalle.getProducto().getCodigo())
                        .cantidad(detalle.getCantidad())
                        .precioUnitario(detalle.getPrecioUnitario())
                        .subtotal(detalle.getSubtotal())
                        .esServicio(detalle.getEsServicio())
                        .descripcion(detalle.getDescripcion())
                        .build())
                .collect(Collectors.toList());

        return VentaDTO.builder()
                .id(venta.getId())
                .numeroBoleta(venta.getNumeroBoleta())
                .fechaVenta(venta.getFechaVenta())
                .total(venta.getTotal())
                .subtotal(venta.getSubtotal())
                .igv(venta.getIgv())
                .cliente(venta.getCliente())
                .documento(venta.getDocumento())
                .observaciones(venta.getObservaciones())
                .detalles(detallesDTO)
                .build();
    }
}
