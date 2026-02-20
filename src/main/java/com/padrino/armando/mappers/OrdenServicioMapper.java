package com.padrino.armando.mappers;

import com.padrino.armando.dtos.orden.DetalleOrdenResponseDTO;
import com.padrino.armando.dtos.orden.OrdenServicioResponseDTO;
import com.padrino.armando.entities.DetalleOrdenServicio;
import com.padrino.armando.entities.OrdenServicio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdenServicioMapper {

    public OrdenServicioResponseDTO toResponseDTO(OrdenServicio orden) {
        List<DetalleOrdenResponseDTO> detallesDTO = orden.getDetalles().stream()
                .map(this::toDetalleDTO)
                .toList();

        return OrdenServicioResponseDTO.builder()
                .id(orden.getId())
                .numeroOrden(orden.getNumeroOrden())
                .fecha(orden.getFecha())
                .ruc(orden.getRuc())
                .cliente(orden.getCliente())
                .placa(orden.getPlaca())
                .formaPago(orden.getFormaPago())
                .total(orden.getTotal())
                .detalles(detallesDTO)
                .build();
    }

    private DetalleOrdenResponseDTO toDetalleDTO(DetalleOrdenServicio detalle) {
        String codigo;
        String descripcion;
        Long llantaId = null;
        Long servicioId = null;

        if (detalle.getLlanta() != null) {
            llantaId = detalle.getLlanta().getId();
            codigo = detalle.getLlanta().getCodigo();
            descripcion = detalle.getLlanta().getProducto();
        } else {
            servicioId = detalle.getServicio().getId();
            codigo = detalle.getServicio().getCodigo();
            descripcion = detalle.getServicio().getProducto();
        }

        return DetalleOrdenResponseDTO.builder()
                .id(detalle.getId())
                .llantaId(llantaId)
                .servicioId(servicioId)
                .codigo(codigo)
                .descripcion(descripcion)
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .precioTotal(detalle.getPrecioTotal())
                // Campos de costo
                .costoUnitario(detalle.getCostoUnitario())
                .costoTotal(detalle.getCostoTotal())
                .ganancia(detalle.getGanancia())
                .margen(detalle.getMargen())
                .build();
    }
}