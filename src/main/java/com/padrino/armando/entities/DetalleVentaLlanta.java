package com.padrino.armando.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta_llanta")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DetalleVentaLlanta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_llanta_id", nullable = false)
    private VentaLlanta ventaLlanta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "llanta_id", nullable = false)
    private Llanta llanta;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "precio_total", precision = 10, scale = 2)
    private BigDecimal precioTotal;

    // === Campos de costeo ===
    @Column(name = "costo_unitario", precision = 10, scale = 2)
    private BigDecimal costoUnitario;

    @Column(name = "costo_total", precision = 12, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "ganancia", precision = 12, scale = 2)
    private BigDecimal ganancia;

    @Column(name = "margen", precision = 5, scale = 4)
    private BigDecimal margen;
}