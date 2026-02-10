package com.padrino.armando.repositories;

import com.padrino.armando.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query(value = "SELECT * FROM ventas WHERE DATE(fecha_venta) = CURRENT_DATE ORDER BY fecha_venta DESC",
            nativeQuery = true)
    List<Venta> findVentasDelDia();

    @Query("SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin ORDER BY v.fechaVenta DESC")
    List<Venta> findByFechaRange(LocalDateTime inicio, LocalDateTime fin);
}