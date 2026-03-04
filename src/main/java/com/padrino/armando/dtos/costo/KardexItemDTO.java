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
    private BigDecimal entradaCosto;   // costo unitario con hasta 4 decimales
    private BigDecimal entradaTotal;   // = entradaCosto * entradaCantidad (mostrado con 2 dec)

    // Columnas de SALIDA
    private Integer salidaCantidad;
    private BigDecimal salidaCosto;    // = costoPromedio vigente (2 decimales)
    private BigDecimal salidaTotal;    // = salidaCosto * salidaCantidad (2 decimales)

    // Columnas de SALDO
    private Integer saldoCantidad;
    private BigDecimal saldoValor;     // mostrado con 2 decimales
    private BigDecimal costoPromedio;  // = saldoValor / saldoCantidad (2 decimales)
}