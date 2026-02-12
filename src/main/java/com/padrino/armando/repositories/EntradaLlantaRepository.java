package com.padrino.armando.repositories;

import com.padrino.armando.entities.EntradaLlanta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntradaLlantaRepository extends JpaRepository<EntradaLlanta, Long> {

    List<EntradaLlanta> findByFechaBetween(LocalDate desde, LocalDate hasta);

    List<EntradaLlanta> findByProveedorContainingIgnoreCase(String proveedor);

    List<EntradaLlanta> findAllByOrderByFechaDesc();
}