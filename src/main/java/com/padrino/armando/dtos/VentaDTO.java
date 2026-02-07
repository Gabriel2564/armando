package com.padrino.armando.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDTO {

    private Long id;
    private String numeroBoleta;
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private String cliente;
    private String documento;
    private String observaciones;

    @NotEmpty(message = "Debe agregar al menos un producto o servicio")
    private List<DetalleVentaDTO> detalles = new ArrayList<>();
}
