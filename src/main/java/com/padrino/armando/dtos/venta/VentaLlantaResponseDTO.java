package com.padrino.armando.dtos.venta;

import com.padrino.armando.enums.FormaPago;
import com.padrino.armando.enums.TipoComprobante;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class VentaLlantaResponseDTO {

    private Long id;
    private String numeroVenta;
    private LocalDate fecha;
    private String ruc;
    private String cliente;
    private String placa;
    private TipoComprobante tipoComprobante;
    private String numeroComprobante;
    private FormaPago formaPago;
    private BigDecimal total;
    private List<DetalleVentaResponseDTO> detalles;
}