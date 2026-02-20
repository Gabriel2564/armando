package com.padrino.armando.entities;

import com.padrino.armando.enums.Moneda;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_entrada_llanta")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DetalleEntradaLlanta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrada_llanta_id", nullable = false)
    private EntradaLlanta entradaLlanta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "llanta_id", nullable = false)
    private Llanta llanta;

    @Column(nullable = false)
    private Integer cantidad;

    // === Campos originales (precio de compra para el total de la factura) ===
    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "precio_total", precision = 10, scale = 2)
    private BigDecimal precioTotal;

    // === Campos nuevos para costeo ===
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda")
    private Moneda moneda;

    @Column(name = "costo_unitario", precision = 10, scale = 2)
    private BigDecimal costoUnitario;

    @Column(name = "tipo_cambio", precision = 10, scale = 4)
    private BigDecimal tipoCambio;

    @Column(name = "costo_unitario_soles", precision = 10, scale = 2)
    private BigDecimal costoUnitarioSoles;

    @Column(name = "costo_total_soles", precision = 12, scale = 2)
    private BigDecimal costoTotalSoles;
}