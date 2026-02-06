package com.padrino.armando.repositories;

import com.padrino.armando.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Producto> findByCategoria(String categoria);

    List<Producto> findByProveedor(String proveedor);

    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo")
    List<Producto> findProductosConStockBajo();

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT p FROM Producto p WHERE " +
            "LOWER(p.codigo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.categoria) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Producto> buscarProductos(@Param("searchTerm") String searchTerm);

    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.categoria IS NOT NULL ORDER BY p.categoria")
    List<String> findAllCategorias();

    @Query("SELECT DISTINCT p.proveedor FROM Producto p WHERE p.proveedor IS NOT NULL ORDER BY p.proveedor")
    List<String> findAllProveedores();

    @Query("SELECT COUNT(p) FROM Producto p WHERE p.stockActual <= p.stockMinimo")
    Long contarProductosConStockBajo();

    List<Producto> findByUbicacion(String ubicacion);
}
