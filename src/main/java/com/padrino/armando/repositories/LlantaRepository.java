package com.padrino.armando.repositories;

import com.padrino.armando.entities.Llanta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LlantaRepository extends JpaRepository<Llanta, Long> {

    List<Llanta> findByActivoTrue();

    Optional<Llanta> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Llanta> findByTipoAndActivoTrue(String tipo);

    @Query("SELECT l FROM Llanta l WHERE l.activo = true AND " +
            "(LOWER(l.codigo) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(l.producto) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(l.medida) LIKE LOWER(CONCAT('%', :filtro, '%')))")
    List<Llanta> buscarPorFiltro(@Param("filtro") String filtro);

    @Query(value = "SELECT COALESCE(SUM(del.cantidad), 0) FROM detalle_entrada_llanta del " +
            "JOIN entradas_llanta el ON del.entrada_llanta_id = el.id " +
            "WHERE del.llanta_id = :llantaId", nativeQuery = true)
    Integer calcularTotalEntradas(@Param("llantaId") Long llantaId);

    @Query(value = "SELECT COALESCE(SUM(dos.cantidad), 0) FROM detalle_orden_servicio dos " +
            "JOIN ordenes_servicio os ON dos.orden_servicio_id = os.id " +
            "WHERE dos.llanta_id = :llantaId", nativeQuery = true)
    Integer calcularTotalSalidas(@Param("llantaId") Long llantaId);
}