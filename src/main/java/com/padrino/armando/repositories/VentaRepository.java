package com.padrino.armando.repositories;

import com.padrino.armando.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    /**
     * Obtener ventas entre dos fechas
     */
    @Query("SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fechaVenta DESC")
    List<Venta> findByFechaRange(@Param("fechaInicio") LocalDateTime fechaInicio,
                                 @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Buscar por número de boleta
     */
    Venta findByNumeroBoleta(String numeroBoleta);

    /**
     * Obtener ventas del día actual usando SQL nativo para PostgreSQL
     */
    @Query(value = "SELECT * FROM ventas WHERE DATE(fecha_venta) = CURRENT_DATE ORDER BY fecha_venta DESC", nativeQuery = true)
    List<Venta> findVentasDelDia();

    /**
     * Obtener ventas de una fecha específica usando SQL nativo
     */
    @Query(value = "SELECT * FROM ventas WHERE DATE(fecha_venta) = :fecha ORDER BY fecha_venta DESC", nativeQuery = true)
    List<Venta> findByFechaNativa(@Param("fecha") LocalDate fecha);
}