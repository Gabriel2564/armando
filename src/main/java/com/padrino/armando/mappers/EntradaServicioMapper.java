package com.padrino.armando.mappers;

import com.padrino.armando.dtos.entrada.DetalleEntradaResponseDTO;
import com.padrino.armando.dtos.entrada.EntradaResponseDTO;
import com.padrino.armando.entities.DetalleEntradaServicio;
import com.padrino.armando.entities.EntradaServicio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntradaServicioMapper {

    public EntradaResponseDTO toResponseDTO(EntradaServicio entrada) {
        List<DetalleEntradaResponseDTO> detallesDTO = entrada.getDetalles().stream()
                .map(this::toDetalleDTO)
                .toList();

        return EntradaResponseDTO.builder()
                .id(entrada.getId())
                .fecha(entrada.getFecha())
                .ruc(entrada.getRuc())
                .proveedor(entrada.getProveedor())
                .factura(entrada.getFactura())
                .formaPago(entrada.getFormaPago())
                .total(entrada.getTotal())
                .detalles(detallesDTO)
                .build();
    }

    private DetalleEntradaResponseDTO toDetalleDTO(DetalleEntradaServicio detalle) {
        return DetalleEntradaResponseDTO.builder()
                .id(detalle.getId())
                .productoId(detalle.getServicio().getId())
                .codigo(detalle.getServicio().getCodigo())
                .descripcion(detalle.getServicio().getProducto())
                .tipoGrupo(detalle.getServicio().getTipo())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .precioTotal(detalle.getPrecioTotal())
                .build();
    }
}