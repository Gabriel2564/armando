package com.padrino.armando.dtos.orden;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DetalleOrdenResponseDTO {

    private Long id;
    private Long llantaId;
    private Long servicioId;
    private String codigo;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal precioTotal;

    // === Campos de costeo ===
    private BigDecimal costoUnitario;
    private BigDecimal costoTotal;
    private BigDecimal ganancia;
    private BigDecimal margen;
}