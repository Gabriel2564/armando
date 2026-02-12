package com.padrino.armando.dtos.entrada;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DetalleEntradaRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    private BigDecimal precioUnitario;
}