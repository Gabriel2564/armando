package com.padrino.armando.dtos.costo;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class KardexItemDTO {

    private LocalDate fecha;
    private String tipo; // ENTRADA o SALIDA

    // Columnas de ENTRADA
    private Integer entradaCantidad;
    private BigDecimal entradaCosto;
    private BigDecimal entradaTotal;

    // Columnas de SALIDA
    private Integer salidaCantidad;
    private BigDecimal salidaCosto;
    private BigDecimal salidaTotal;

    // Columnas de SALDO
    private Integer saldoCantidad;
    private BigDecimal saldoValor;
    private BigDecimal costoPromedio;
}