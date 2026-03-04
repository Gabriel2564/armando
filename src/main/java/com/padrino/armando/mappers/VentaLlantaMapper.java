package com.padrino.armando.mappers;

import com.padrino.armando.dtos.venta.DetalleVentaResponseDTO;
import com.padrino.armando.dtos.venta.VentaLlantaResponseDTO;
import com.padrino.armando.entities.DetalleVentaLlanta;
import com.padrino.armando.entities.VentaLlanta;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VentaLlantaMapper {

    public VentaLlantaResponseDTO toResponseDTO(VentaLlanta venta) {
        List<DetalleVentaResponseDTO> detallesDTO = venta.getDetalles().stream()
                .map(this::toDetalleDTO)
                .toList();

        return VentaLlantaResponseDTO.builder()
                .id(venta.getId())
                .numeroVenta(venta.getNumeroVenta())
                .fecha(venta.getFecha())
                .ruc(venta.getRuc())
                .cliente(venta.getCliente())
                .placa(venta.getPlaca())
                .tipoComprobante(venta.getTipoComprobante())
                .numeroComprobante(venta.getNumeroComprobante())
                .formaPago(venta.getFormaPago())
                .total(venta.getTotal())
                .detalles(detallesDTO)
                .build();
    }

    private DetalleVentaResponseDTO toDetalleDTO(DetalleVentaLlanta detalle) {
        return DetalleVentaResponseDTO.builder()
                .id(detalle.getId())
                .llantaId(detalle.getLlanta().getId())
                .codigo(detalle.getLlanta().getCodigo())
                .descripcion(detalle.getLlanta().getProducto())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .precioTotal(detalle.getPrecioTotal())
                .costoUnitario(detalle.getCostoUnitario())
                .costoTotal(detalle.getCostoTotal())
                .ganancia(detalle.getGanancia())
                .margen(detalle.getMargen())
                .build();
    }
}