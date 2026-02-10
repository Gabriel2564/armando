package com.padrino.armando.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoteDTO {

    private Long id;

    @NotNull(message = "El producto es obligatorio")
    private Long productoId;

    private String productoCodigo;
    private String productoNombre;
    private String productoTipo;

    private String numeroLote;

    @NotNull(message = "El stock inicial es obligatorio")
    @Min(value = 1, message = "El stock debe ser al menos 1")
    private Integer stockInicial;

    private Integer stockActual;

    @DecimalMin(value = "0.0", message = "El precio de compra debe ser mayor o igual a 0")
    private BigDecimal precioCompra;

    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaTerminacion;

    private String estado; // ACTIVO, AGOTADO
    private String observaciones;

    // Campos calculados
    private Integer stockVendido;
    private Long diasActivo;
}