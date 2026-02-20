package com.padrino.armando.dtos.entrada;

import com.padrino.armando.enums.Moneda;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DetalleEntradaResponseDTO {

    private Long id;
    private Long productoId;
    private String codigo;
    private String descripcion;
    private String tipoGrupo;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal precioTotal;

    // === Campos de costeo ===
    private Moneda moneda;
    private BigDecimal costoUnitario;
    private BigDecimal tipoCambio;
    private BigDecimal costoUnitarioSoles;
    private BigDecimal costoTotalSoles;
}