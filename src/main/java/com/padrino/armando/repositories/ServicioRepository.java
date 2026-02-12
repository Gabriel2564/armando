package com.padrino.armando.repositories;

import com.padrino.armando.entities.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByActivoTrue();

    Optional<Servicio> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Servicio> findByTipoAndActivoTrue(String tipo);

    @Query("SELECT s FROM Servicio s WHERE s.activo = true AND " +
            "(LOWER(s.codigo) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(s.producto) LIKE LOWER(CONCAT('%', :filtro, '%')))")
    List<Servicio> buscarPorFiltro(@Param("filtro") String filtro);

    @Query(value = "SELECT COALESCE(SUM(des.cantidad), 0) FROM detalle_entrada_servicio des " +
            "JOIN entradas_servicio es ON des.entrada_servicio_id = es.id " +
            "WHERE des.servicio_id = :servicioId", nativeQuery = true)
    Integer calcularTotalEntradas(@Param("servicioId") Long servicioId);

    @Query(value = "SELECT COALESCE(SUM(dos.cantidad), 0) FROM detalle_orden_servicio dos " +
            "JOIN ordenes_servicio os ON dos.orden_servicio_id = os.id " +
            "WHERE dos.servicio_id = :servicioId", nativeQuery = true)
    Integer calcularTotalSalidas(@Param("servicioId") Long servicioId);
}