package com.padrino.armando.entities;

import com.padrino.armando.enums.FormaPago;
import com.padrino.armando.enums.TipoComprobante;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas_llanta")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class VentaLlanta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_venta", nullable = false, unique = true)
    private String numeroVenta;

    @Column(nullable = false)
    private LocalDate fecha;

    private String ruc;

    private String cliente;

    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante")
    private TipoComprobante tipoComprobante;

    @Column(name = "numero_comprobante")
    private String numeroComprobante;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago")
    private FormaPago formaPago;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "ventaLlanta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleVentaLlanta> detalles = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}