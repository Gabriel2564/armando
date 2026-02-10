package com.padrino.armando.config;

import com.padrino.armando.entities.Lote;
import com.padrino.armando.entities.Producto;
import com.padrino.armando.repositories.LoteRepository;
import com.padrino.armando.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Carga datos maestros en la base de datos al iniciar la aplicación
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;

    @Override
    public void run(String... args) {
        if (productoRepository.count() > 0) {
            log.info("La base de datos ya contiene productos. No se cargarán datos maestros.");
            return;
        }

        log.info("========================================");
        log.info("  CARGANDO DATOS MAESTROS");
        log.info("========================================");

        cargarProductosMaestros();
        cargarLotesIniciales();

        log.info("========================================");
        log.info("  ✅ DATOS MAESTROS CARGADOS");
        log.info("========================================");
    }

    private void cargarProductosMaestros() {
        log.info("Cargando productos maestros...");

        List<Producto> productosMaestros = Arrays.asList(
                // ========== PARCHES RUZZI ==========
                crearProducto("RPD3", "PARCHE RUZZI RPD3",
                        "Parche de reparación RUZZI modelo RPD3", "PARCHE", 5.00),

                // ========== PARCHES VIPAL RO (Radial) ==========
                crearProducto("RO1", "PARCHE VIPAL RO1",
                        "Parche de reparación VIPAL modelo RO1", "PARCHE", 7.00),
                crearProducto("RO3", "PARCHE VIPAL RO3",
                        "Parche de reparación VIPAL modelo RO3", "PARCHE", 8.00),
                crearProducto("RO4", "PARCHE VIPAL RO4",
                        "Parche de reparación VIPAL modelo RO4", "PARCHE", 10.00),

                // ========== PARCHES VIPAL VD (Diagonal) ==========
                crearProducto("VD1", "PARCHE VIPAL VD1",
                        "Parche de reparación VIPAL modelo VD1", "PARCHE", 12.00),

                // ========== PARCHES VIPAL RACI (Radial Camión) ==========
                crearProducto("RACI0", "PARCHE VIPAL RACI0",
                        "Parche de reparación VIPAL modelo RACI0", "PARCHE", 15.00),
                crearProducto("RACI2", "PARCHE VIPAL RACI2",
                        "Parche de reparación VIPAL modelo RACI2", "PARCHE", 18.00),
                crearProducto("RACI4", "PARCHE VIPAL RACI4",
                        "Parche de reparación VIPAL modelo RACI4", "PARCHE", 22.00),
                crearProducto("RACI20", "PARCHE VIPAL RACI20",
                        "Parche de reparación VIPAL modelo RACI20 - Tamaño grande", "PARCHE", 95.00),

                // ========== MATERIALES ==========
                crearProducto("PEGAMENTO-500ML", "Pegamento para Parches 500ml",
                        "Pegamento especial para parches de llantas", "MATERIAL", 25.00),
                crearProducto("SOLUCION-LIMPIEZA", "Solución de Limpieza",
                        "Solución para limpiar superficie antes de parchar", "MATERIAL", 15.00),

                // ========== ACCESORIOS ==========
                crearProducto("VALVULA-STD", "Válvula Estándar",
                        "Válvula estándar para llantas", "ACCESORIO", 5.00),
                crearProducto("TAPA-VALVULA", "Tapa de Válvula",
                        "Tapa protectora de válvula", "ACCESORIO", 1.50),
                crearProducto("VALVULA-METAL", "Válvula Metálica",
                        "Válvula metálica resistente", "ACCESORIO", 8.00),

                // ========== SERVICIOS ==========
                crearProducto("SERV-PARCHADO", "Servicio de Parchado",
                        "Servicio de parchado de llanta (precio variable)", "SERVICIO", 0.00),
                crearProducto("SERV-ALINEACION", "Servicio de Alineación",
                        "Servicio de alineación de llantas", "SERVICIO", 50.00),
                crearProducto("SERV-BALANCEO", "Servicio de Balanceo",
                        "Servicio de balanceo de llantas", "SERVICIO", 40.00),
                crearProducto("SERV-ROTACION", "Servicio de Rotación",
                        "Servicio de rotación de llantas", "SERVICIO", 30.00),
                crearProducto("SERV-VALVULA", "Cambio de Válvula",
                        "Servicio de cambio de válvula", "SERVICIO", 15.00)
        );

        productoRepository.saveAll(productosMaestros);
        log.info("✅ {} productos maestros cargados", productosMaestros.size());
    }

    private void cargarLotesIniciales() {
        log.info("Cargando lotes iniciales de stock...");

        // Crear lotes para cada producto (excepto servicios)
        List<Producto> productos = productoRepository.findAll();
        int totalLotes = 0;

        for (Producto producto : productos) {
            if ("SERVICIO".equals(producto.getTipo())) {
                continue; // Los servicios no tienen stock físico
            }

            // Determinar stock inicial según el tipo
            int stockInicial;
            BigDecimal precioCompra;

            switch (producto.getTipo()) {
                case "PARCHE":
                    stockInicial = 50;
                    precioCompra = producto.getPrecioVenta().multiply(new BigDecimal("0.60")); // 60% del precio venta
                    break;
                case "MATERIAL":
                    stockInicial = 20;
                    precioCompra = producto.getPrecioVenta().multiply(new BigDecimal("0.65"));
                    break;
                case "ACCESORIO":
                    stockInicial = 100;
                    precioCompra = producto.getPrecioVenta().multiply(new BigDecimal("0.50"));
                    break;
                default:
                    stockInicial = 30;
                    precioCompra = producto.getPrecioVenta().multiply(new BigDecimal("0.70"));
            }

            Lote lote = new Lote();
            lote.setProducto(producto);
            lote.setNumeroLote("LOTE-" + producto.getCodigo() + "-001");
            lote.setStockInicial(stockInicial);
            lote.setStockActual(stockInicial);
            lote.setPrecioCompra(precioCompra);
            lote.setFechaEntrada(LocalDateTime.now());
            lote.setEstado("ACTIVO");
            lote.setObservaciones("Lote inicial - Carga de datos maestros");

            loteRepository.save(lote);
            totalLotes++;
        }

        log.info("✅ {} lotes iniciales creados", totalLotes);
    }

    private Producto crearProducto(String codigo, String nombre, String descripcion,
                                   String tipo, double precioVenta) {
        Producto producto = new Producto();
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setTipo(tipo);
        producto.setPrecioVenta(new BigDecimal(precioVenta));
        return producto;
    }
}