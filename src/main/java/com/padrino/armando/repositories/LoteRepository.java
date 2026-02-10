package com.padrino.armando.repositories;

import com.padrino.armando.entities.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {

    List<Lote> findByProductoIdOrderByFechaEntradaAsc(Long productoId);

    @Query("SELECT l FROM Lote l WHERE l.producto.id = :productoId AND l.estado = 'ACTIVO' " +
            "AND l.stockActual > 0 ORDER BY l.fechaEntrada ASC")
    List<Lote> findLotesActivosByProducto(@Param("productoId") Long productoId);

    @Query("SELECT l FROM Lote l WHERE l.estado = 'ACTIVO' ORDER BY l.producto.codigo, l.fechaEntrada")
    List<Lote> findAllActivos();

    @Query("SELECT l FROM Lote l WHERE l.producto.tipo = :tipo ORDER BY l.producto.codigo, l.fechaEntrada")
    List<Lote> findByTipo(@Param("tipo") String tipo);
}