package com.padrino.armando.dtos.llanta;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LlantaResponseDTO {

    private Long id;
    private String codigo;
    private String producto;
    private String medida;
    private String tipo;
    private String proveedor;
    private Integer entradas;
    private Integer salidas;
    private Integer stock;
}