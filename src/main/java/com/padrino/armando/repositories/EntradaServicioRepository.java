package com.padrino.armando.repositories;

import com.padrino.armando.entities.EntradaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntradaServicioRepository extends JpaRepository<EntradaServicio, Long> {

    List<EntradaServicio> findByFechaBetween(LocalDate desde, LocalDate hasta);

    List<EntradaServicio> findByProveedorContainingIgnoreCase(String proveedor);

    List<EntradaServicio> findAllByOrderByFechaDesc();
}