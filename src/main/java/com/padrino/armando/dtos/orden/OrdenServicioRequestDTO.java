package com.padrino.armando.dtos.orden;

import com.padrino.armando.enums.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrdenServicioRequestDTO {

    @NotBlank(message = "El número de orden es obligatorio")
    private String numeroOrden;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String ruc;
    private String cliente;
    private String placa;
    private FormaPago formaPago;

    @NotEmpty(message = "Debe incluir al menos un detalle")
    @Valid
    private List<DetalleOrdenRequestDTO> detalles;
}