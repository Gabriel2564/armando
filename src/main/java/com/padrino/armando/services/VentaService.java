package com.padrino.armando.services;

import com.padrino.armando.dtos.DetalleVentaDTO;
import com.padrino.armando.dtos.VentaDTO;
import com.padrino.armando.entities.DetalleVenta;
import com.padrino.armando.entities.Lote;
import com.padrino.armando.entities.Producto;
import com.padrino.armando.entities.Venta;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.repositories.LoteRepository;
import com.padrino.armando.repositories.ProductoRepository;
import com.padrino.armando.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;
    private final LoteService loteService;

    public VentaDTO registrarVenta(VentaDTO ventaDTO) {
        log.info("Registrando nueva venta");

        Venta venta = new Venta();
        venta.setCliente(ventaDTO.getCliente());
        venta.setObservaciones(ventaDTO.getObservaciones());

        boolean hayParches = false;

        // Procesar detalles
        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", detalleDTO.getProductoId()));

            // Verificar si es parche
            if ("PARCHE".equals(producto.getTipo())) {
                hayParches = true;
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setEsServicio(detalleDTO.getEsServicio());

            if (detalleDTO.getPrecioManual() != null) {
                // Precio manual (servicio de parchado)
                detalle.setPrecioManual(detalleDTO.getPrecioManual());
                detalle.setPrecioUnitario(detalleDTO.getPrecioManual());
            } else {
                detalle.setPrecioUnitario(producto.getPrecioVenta());
            }

            // Si NO es servicio, descontar del lote más antiguo (FIFO)
            if (!detalleDTO.getEsServicio()) {
                List<Lote> lotesActivos = loteRepository.findLotesActivosByProducto(producto.getId());

                if (lotesActivos.isEmpty()) {
                    throw new IllegalArgumentException("No hay stock disponible para: " + producto.getNombre());
                }

                int cantidadPendiente = detalleDTO.getCantidad();

                for (Lote lote : lotesActivos) {
                    if (cantidadPendiente <= 0) break;

                    int cantidadDescontar = Math.min(cantidadPendiente, lote.getStockActual());

                    loteService.descontarStock(lote.getId(), cantidadDescontar);
                    cantidadPendiente -= cantidadDescontar;

                    // Asignar el lote al detalle (usamos el primer lote por simplicidad)
                    if (detalle.getLote() == null) {
                        detalle.setLote(lote);
                    }
                }

                if (cantidadPendiente > 0) {
                    throw new IllegalArgumentException("Stock insuficiente para: " + producto.getNombre());
                }
            }

            detalle.calcularSubtotal();
            detalle.setVenta(venta);
            venta.getDetalles().add(detalle);
        }

        // Si hay parches y NO hay servicio de parchado, agregarlo
        if (hayParches) {
            boolean tieneParchado = ventaDTO.getDetalles().stream()
                    .anyMatch(d -> d.getProductoCodigo() != null && d.getProductoCodigo().equals("SERV-PARCHADO"));

            if (!tieneParchado) {
                log.info("Agregando servicio de parchado automáticamente");

                // Buscar el producto de servicio de parchado
                Producto servicioParchado = productoRepository.findByCodigo("SERV-PARCHADO")
                        .orElseThrow(() -> new ResourceNotFoundException("Producto", "codigo", "SERV-PARCHADO"));

                DetalleVenta detalleParchado = new DetalleVenta();
                detalleParchado.setProducto(servicioParchado);
                detalleParchado.setCantidad(1);
                detalleParchado.setEsServicio(true);
                // El precio se debe ingresar manualmente desde el frontend
                detalleParchado.setPrecioUnitario(BigDecimal.ZERO); // Se actualizará con precio manual
                detalleParchado.setPrecioManual(BigDecimal.ZERO);
                detalleParchado.calcularSubtotal();
                detalleParchado.setVenta(venta);
                venta.getDetalles().add(detalleParchado);
            }
        }

        // Calcular totales
        venta.calcularTotal();

        // Guardar venta
        Venta ventaGuardada = ventaRepository.save(venta);
        log.info("Venta registrada exitosamente. Número: {}", ventaGuardada.getNumeroBoleta());

        return convertirADTO(ventaGuardada);
    }

    public List<VentaDTO> obtenerVentasDelDia() {
        log.info("Obteniendo ventas del día");
        return ventaRepository.findVentasDelDia().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public VentaDTO obtenerPorId(Long id) {
        log.info("Obteniendo venta por ID: {}", id);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));
        return convertirADTO(venta);
    }

    private VentaDTO convertirADTO(Venta venta) {
        List<DetalleVentaDTO> detallesDTO = venta.getDetalles().stream()
                .map(detalle -> DetalleVentaDTO.builder()
                        .id(detalle.getId())
                        .productoId(detalle.getProducto().getId())
                        .productoCodigo(detalle.getProducto().getCodigo())
                        .productoNombre(detalle.getProducto().getNombre())
                        .cantidad(detalle.getCantidad())
                        .precioUnitario(detalle.getPrecioUnitario())
                        .subtotal(detalle.getSubtotal())
                        .esServicio(detalle.getEsServicio())
                        .precioManual(detalle.getPrecioManual())
                        .build())
                .collect(Collectors.toList());

        return VentaDTO.builder()
                .id(venta.getId())
                .numeroBoleta(venta.getNumeroBoleta())
                .fechaVenta(venta.getFechaVenta())
                .total(venta.getTotal())
                .cliente(venta.getCliente())
                .observaciones(venta.getObservaciones())
                .detalles(detallesDTO)
                .build();
    }
}