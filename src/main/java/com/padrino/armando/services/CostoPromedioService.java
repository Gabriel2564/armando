package com.padrino.armando.services;

import com.padrino.armando.dtos.costo.KardexItemDTO;
import com.padrino.armando.dtos.costo.ResumenCostoDTO;
import com.padrino.armando.entities.DetalleEntradaLlanta;
import com.padrino.armando.entities.DetalleOrdenServicio;
import com.padrino.armando.entities.Llanta;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.repositories.DetalleEntradaLlantaRepository;
import com.padrino.armando.repositories.DetalleOrdenServicioRepository;
import com.padrino.armando.repositories.LlantaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CostoPromedioService {

    private final DetalleEntradaLlantaRepository detalleEntradaLlantaRepository;
    private final DetalleOrdenServicioRepository detalleOrdenServicioRepository;
    private final LlantaRepository llantaRepository;

    /**
     * Calcula el costo promedio actual de una llanta recorriendo
     * cronologicamente todas las entradas y salidas (Promedio Ponderado Movil).
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularCostoPromedio(Long llantaId) {
        List<KardexItemDTO> kardex = construirKardex(llantaId);
        if (kardex.isEmpty()) return BigDecimal.ZERO;

        KardexItemDTO ultimo = kardex.get(kardex.size() - 1);
        return ultimo.getCostoPromedio();
    }

    /**
     * Construye el Kardex completo de un producto (llanta):
     * lista cronologica de entradas y salidas con costo promedio movil.
     */
    @Transactional(readOnly = true)
    public List<KardexItemDTO> construirKardex(Long llantaId) {
        Llanta llanta = llantaRepository.findById(llantaId)
                .orElseThrow(() -> new ResourceNotFoundException("Llanta no encontrada con ID: " + llantaId));

        // Obtener todas las entradas de esta llanta
        List<DetalleEntradaLlanta> entradas = detalleEntradaLlantaRepository.findByLlantaIdOrderByFecha(llantaId);

        // Obtener todas las salidas de esta llanta
        List<DetalleOrdenServicio> salidas = detalleOrdenServicioRepository.findByLlantaIdOrderByFecha(llantaId);

        // Construir lista unificada de movimientos
        List<Movimiento> movimientos = new ArrayList<>();

        for (DetalleEntradaLlanta e : entradas) {
            BigDecimal costoUnit = e.getCostoUnitarioSoles() != null ? e.getCostoUnitarioSoles() : e.getPrecioUnitario();
            movimientos.add(new Movimiento(
                    e.getEntradaLlanta().getFecha(),
                    e.getEntradaLlanta().getId(),
                    "ENTRADA",
                    e.getCantidad(),
                    costoUnit != null ? costoUnit : BigDecimal.ZERO
            ));
        }

        for (DetalleOrdenServicio s : salidas) {
            movimientos.add(new Movimiento(
                    s.getOrdenServicio().getFecha(),
                    s.getOrdenServicio().getId(),
                    "SALIDA",
                    s.getCantidad(),
                    null // el costo se calcula en el recorrido
            ));
        }

        // Ordenar por fecha, y dentro de la misma fecha: entradas primero
        movimientos.sort(Comparator
                .comparing(Movimiento::fecha)
                .thenComparing(m -> m.tipo().equals("ENTRADA") ? 0 : 1)
                .thenComparing(Movimiento::documentoId));

        // Recorrer y calcular costo promedio movil
        List<KardexItemDTO> kardex = new ArrayList<>();
        BigDecimal saldoValor = BigDecimal.ZERO;
        int saldoCantidad = 0;
        BigDecimal costoPromedio = BigDecimal.ZERO;

        for (Movimiento mov : movimientos) {
            KardexItemDTO item = new KardexItemDTO();
            item.setFecha(mov.fecha());
            item.setTipo(mov.tipo());

            if ("ENTRADA".equals(mov.tipo())) {
                item.setEntradaCantidad(mov.cantidad());
                item.setEntradaCosto(mov.costoUnitario());
                BigDecimal entradaTotal = mov.costoUnitario().multiply(BigDecimal.valueOf(mov.cantidad()));
                item.setEntradaTotal(entradaTotal);

                saldoValor = saldoValor.add(entradaTotal);
                saldoCantidad += mov.cantidad();

            } else { // SALIDA
                // Usar el costo promedio vigente
                item.setSalidaCantidad(mov.cantidad());
                item.setSalidaCosto(costoPromedio);
                BigDecimal salidaTotal = costoPromedio.multiply(BigDecimal.valueOf(mov.cantidad()));
                item.setSalidaTotal(salidaTotal);

                saldoValor = saldoValor.subtract(salidaTotal);
                saldoCantidad -= mov.cantidad();
            }

            item.setSaldoCantidad(saldoCantidad);
            item.setSaldoValor(saldoValor);

            if (saldoCantidad > 0) {
                costoPromedio = saldoValor.divide(BigDecimal.valueOf(saldoCantidad), 2, RoundingMode.HALF_UP);
            } else {
                costoPromedio = BigDecimal.ZERO;
                saldoValor = BigDecimal.ZERO; // Reset cuando llega a 0
            }
            item.setCostoPromedio(costoPromedio);

            kardex.add(item);
        }

        return kardex;
    }

    /**
     * Resumen de costos para mostrar en el stock de llantas.
     */
    @Transactional(readOnly = true)
    public ResumenCostoDTO obtenerResumenCosto(Long llantaId) {
        List<KardexItemDTO> kardex = construirKardex(llantaId);

        ResumenCostoDTO resumen = new ResumenCostoDTO();
        resumen.setLlantaId(llantaId);

        if (kardex.isEmpty()) {
            resumen.setCostoPromedio(BigDecimal.ZERO);
            resumen.setStockActual(0);
            resumen.setValorInventario(BigDecimal.ZERO);
            return resumen;
        }

        KardexItemDTO ultimo = kardex.get(kardex.size() - 1);
        resumen.setCostoPromedio(ultimo.getCostoPromedio());
        resumen.setStockActual(ultimo.getSaldoCantidad());
        resumen.setValorInventario(ultimo.getSaldoValor());
        return resumen;
    }

    // Record interno para unificar movimientos
    private record Movimiento(LocalDate fecha, Long documentoId, String tipo, int cantidad, BigDecimal costoUnitario) {}
}