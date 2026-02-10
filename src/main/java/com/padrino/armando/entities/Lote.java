package com.padrino.armando.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "numero_lote", nullable = false, length = 100)
    private String numeroLote; // Ej: LOTE-RPD3-2024-001

    @Column(name = "stock_inicial", nullable = false)
    private Integer stockInicial;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "fecha_entrada", nullable = false)
    private LocalDateTime fechaEntrada;

    @Column(name = "fecha_terminacion")
    private LocalDateTime fechaTerminacion;

    @Column(name = "estado", length = 20)
    private String estado; // ACTIVO, AGOTADO

    @Column(length = 500)
    private String observaciones;

    @PrePersist
    protected void onCreate() {
        if (fechaEntrada == null) {
            fechaEntrada = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "ACTIVO";
        }
        if (stockActual == null) {
            stockActual = stockInicial;
        }
    }

    public void descontarStock(Integer cantidad) {
        this.stockActual -= cantidad;
        if (this.stockActual <= 0) {
            this.estado = "AGOTADO";
            this.fechaTerminacion = LocalDateTime.now();
            this.stockActual = 0;
        }
    }

    @Transient
    public Integer getStockVendido() {
        return stockInicial - stockActual;
    }

    @Transient
    public Long getDiasActivo() {
        if (fechaTerminacion != null) {
            return java.time.Duration.between(fechaEntrada, fechaTerminacion).toDays();
        }
        return java.time.Duration.between(fechaEntrada, LocalDateTime.now()).toDays();
    }
}