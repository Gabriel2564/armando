package com.padrino.armando.dtos.servicio;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ServicioResponseDTO {

    private Long id;
    private String codigo;
    private String producto;
    private String tipo;
    private BigDecimal precio;
    private Integer entradas;
    private Integer salidas;
    private Integer stock;
}