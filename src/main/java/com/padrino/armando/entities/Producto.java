package com.padrino.armando.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 100)
    private String categoria;

    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "precio_venta", precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(length = 200)
    private String proveedor;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(length = 200)
    private String ubicacion;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        if (stockMinimo == null) {
            stockMinimo = 0;
        }
        if (precioCompra == null) {
            precioCompra = BigDecimal.ZERO;
        }
        if (precioVenta == null) {
            precioVenta = BigDecimal.ZERO;
        }
    }

    @Transient
    public boolean isStockBajo() {
        return stockActual != null && stockMinimo != null && stockActual <= stockMinimo;
    }

    @Transient
    public BigDecimal getMargenGanancia() {
        if (precioCompra != null && precioVenta != null &&
                precioCompra.compareTo(BigDecimal.ZERO) > 0) {
            return precioVenta.subtract(precioCompra);
        }
        return BigDecimal.ZERO;
    }

    @Transient
    public BigDecimal getPorcentajeMargen() {
        if (precioCompra != null && precioCompra.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal margen = getMargenGanancia();
            return margen.divide(precioCompra, 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }
}
