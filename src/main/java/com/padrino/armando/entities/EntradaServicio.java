package com.padrino.armando.entities;

import com.padrino.armando.enums.FormaPago;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entradas_servicio")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EntradaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    private String ruc;

    private String proveedor;

    private String factura;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago")
    private FormaPago formaPago;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "entradaServicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleEntradaServicio> detalles = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}