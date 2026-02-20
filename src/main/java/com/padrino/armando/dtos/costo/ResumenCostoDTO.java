package com.padrino.armando.dtos.costo;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ResumenCostoDTO {

    private Long llantaId;
    private BigDecimal costoPromedio;
    private Integer stockActual;
    private BigDecimal valorInventario;
}