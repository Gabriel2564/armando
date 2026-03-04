package com.padrino.armando.dtos.venta;

import com.padrino.armando.enums.FormaPago;
import com.padrino.armando.enums.TipoComprobante;
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
public class VentaLlantaRequestDTO {

    @NotBlank(message = "El número de venta es obligatorio")
    private String numeroVenta;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String ruc;

    private String cliente;

    private String placa;

    private TipoComprobante tipoComprobante;

    private String numeroComprobante;

    @NotNull(message = "La forma de pago es obligatoria")
    private FormaPago formaPago;

    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid
    private List<DetalleVentaRequestDTO> detalles;
}