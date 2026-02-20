package com.padrino.armando.repositories;

import com.padrino.armando.entities.DetalleOrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleOrdenServicioRepository extends JpaRepository<DetalleOrdenServicio, Long> {

    @Query("SELECT d FROM DetalleOrdenServicio d JOIN d.ordenServicio o " +
            "WHERE d.llanta.id = :llantaId ORDER BY o.fecha ASC, o.id ASC")
    List<DetalleOrdenServicio> findByLlantaIdOrderByFecha(@Param("llantaId") Long llantaId);
}