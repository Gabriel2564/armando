package com.padrino.armando.entities;

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

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "precio_total", precision = 10, scale = 2)
    private BigDecimal precioTotal;
}