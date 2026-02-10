package com.padrino.armando.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lote_id")
    private Lote lote; // De qué lote se vendió

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "es_servicio")
    private Boolean esServicio = false;

    @Column(name = "precio_manual", precision = 10, scale = 2)
    private BigDecimal precioManual; // Para parchado con precio variable

    public void calcularSubtotal() {
        if (precioManual != null) {
            subtotal = precioManual;
        } else if (cantidad != null && precioUnitario != null) {
            subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
        } else {
            subtotal = BigDecimal.ZERO;
        }
    }
}