package com.padrino.armando.repositories;

import com.padrino.armando.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo);

    List<Producto> findByTipo(String tipo);

    @Query("SELECT DISTINCT p.tipo FROM Producto p ORDER BY p.tipo")
    List<String> findAllTipos();

    @Query("SELECT p FROM Producto p WHERE LOWER(p.codigo) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Producto> buscarPorCodigoONombre(String term);
}