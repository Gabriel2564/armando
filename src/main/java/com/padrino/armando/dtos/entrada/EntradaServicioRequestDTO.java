package com.padrino.armando.dtos.entrada;

import com.padrino.armando.enums.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EntradaServicioRequestDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String ruc;
    private String proveedor;
    private String factura;
    private FormaPago formaPago;

    @NotEmpty(message = "Debe incluir al menos un detalle")
    @Valid
    private List<DetalleEntradaRequestDTO> detalles;
}