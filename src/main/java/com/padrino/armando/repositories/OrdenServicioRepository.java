package com.padrino.armando.repositories;

import com.padrino.armando.entities.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {

    Optional<OrdenServicio> findByNumeroOrden(String numeroOrden);

    boolean existsByNumeroOrden(String numeroOrden);

    List<OrdenServicio> findByFechaBetween(LocalDate desde, LocalDate hasta);

    List<OrdenServicio> findByClienteContainingIgnoreCase(String cliente);

    List<OrdenServicio> findByPlacaContainingIgnoreCase(String placa);

    List<OrdenServicio> findAllByOrderByFechaDesc();
}