package com.padrino.armando.repositories;

import com.padrino.armando.entities.DetalleEntradaLlanta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleEntradaLlantaRepository extends JpaRepository<DetalleEntradaLlanta, Long> {

    @Query("SELECT d FROM DetalleEntradaLlanta d JOIN d.entradaLlanta e " +
            "WHERE d.llanta.id = :llantaId ORDER BY e.fecha ASC, e.id ASC")
    List<DetalleEntradaLlanta> findByLlantaIdOrderByFecha(@Param("llantaId") Long llantaId);
}