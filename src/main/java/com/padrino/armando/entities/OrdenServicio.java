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
@Table(name = "ordenes_servicio")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_orden", nullable = false, unique = true)
    private String numeroOrden;

    @Column(nullable = false)
    private LocalDate fecha;

    private String ruc;

    private String cliente;

    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago")
    private FormaPago formaPago;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "ordenServicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleOrdenServicio> detalles = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}