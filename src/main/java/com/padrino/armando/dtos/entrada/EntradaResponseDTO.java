package com.padrino.armando.dtos.entrada;

import com.padrino.armando.enums.FormaPago;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EntradaResponseDTO {

    private Long id;
    private LocalDate fecha;
    private String ruc;
    private String proveedor;
    private String factura;
    private FormaPago formaPago;
    private BigDecimal total;
    private List<DetalleEntradaResponseDTO> detalles;
}