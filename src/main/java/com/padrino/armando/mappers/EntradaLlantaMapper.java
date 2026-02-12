package com.padrino.armando.mappers;

import com.padrino.armando.dtos.entrada.DetalleEntradaResponseDTO;
import com.padrino.armando.dtos.entrada.EntradaResponseDTO;
import com.padrino.armando.entities.DetalleEntradaLlanta;
import com.padrino.armando.entities.EntradaLlanta;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntradaLlantaMapper {

    public EntradaResponseDTO toResponseDTO(EntradaLlanta entrada) {
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

    private DetalleEntradaResponseDTO toDetalleDTO(DetalleEntradaLlanta detalle) {
        return DetalleEntradaResponseDTO.builder()
                .id(detalle.getId())
                .productoId(detalle.getLlanta().getId())
                .codigo(detalle.getLlanta().getCodigo())
                .descripcion(detalle.getLlanta().getProducto())
                .tipoGrupo(detalle.getLlanta().getTipo())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .precioTotal(detalle.getPrecioTotal())
                .build();
    }
}