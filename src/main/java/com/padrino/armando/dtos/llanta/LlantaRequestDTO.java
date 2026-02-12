package com.padrino.armando.dtos.llanta;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LlantaRequestDTO {

    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @NotBlank(message = "El producto es obligatorio")
    private String producto;

    private String medida;
    private String tipo;
    private String proveedor;
}