package com.padrino.armando.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_entrada_servicio")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DetalleEntradaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrada_servicio_id", nullable = false)
    private EntradaServicio entradaServicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "precio_total", precision = 10, scale = 2)
    private BigDecimal precioTotal;
}