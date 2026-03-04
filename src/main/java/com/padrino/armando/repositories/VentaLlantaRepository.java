package com.padrino.armando.repositories;

import com.padrino.armando.entities.VentaLlanta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaLlantaRepository extends JpaRepository<VentaLlanta, Long> {

    Optional<VentaLlanta> findByNumeroVenta(String numeroVenta);

    boolean existsByNumeroVenta(String numeroVenta);

    List<VentaLlanta> findByFechaBetween(LocalDate desde, LocalDate hasta);

    List<VentaLlanta> findByClienteContainingIgnoreCase(String cliente);

    List<VentaLlanta> findByPlacaContainingIgnoreCase(String placa);

    List<VentaLlanta> findAllByOrderByFechaDesc();

    @Query(value = "SELECT COALESCE(SUM(dvl.cantidad), 0) FROM detalle_venta_llanta dvl " +
            "WHERE dvl.llanta_id = :llantaId", nativeQuery = true)
    Integer calcularTotalVentas(@Param("llantaId") Long llantaId);
}