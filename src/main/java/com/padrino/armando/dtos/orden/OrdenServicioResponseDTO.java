package com.padrino.armando.dtos.orden;

import com.padrino.armando.enums.FormaPago;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrdenServicioResponseDTO {

    private Long id;
    private String numeroOrden;
    private LocalDate fecha;
    private String ruc;
    private String cliente;
    private String placa;
    private FormaPago formaPago;
    private BigDecimal total;
    private List<DetalleOrdenResponseDTO> detalles;
}