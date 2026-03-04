package com.padrino.armando.dtos.venta;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DetalleVentaResponseDTO {

    private Long id;
    private Long llantaId;
    private String codigo;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal precioTotal;
    private BigDecimal costoUnitario;
    private BigDecimal costoTotal;
    private BigDecimal ganancia;
    private BigDecimal margen;
}