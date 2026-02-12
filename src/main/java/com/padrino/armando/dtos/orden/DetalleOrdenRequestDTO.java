package com.padrino.armando.dtos.orden;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DetalleOrdenRequestDTO {

    private Long llantaId;
    private Long servicioId;

    @NotNull @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    private BigDecimal precioUnitario;
}