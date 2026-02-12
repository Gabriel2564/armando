package com.padrino.armando.dtos.servicio;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ServicioRequestDTO {

    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @NotBlank(message = "El producto es obligatorio")
    private String producto;

    private String tipo;
    private BigDecimal precio;
}