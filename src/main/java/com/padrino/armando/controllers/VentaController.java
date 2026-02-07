package com.padrino.armando.controllers;

import com.padrino.armando.dtos.VentaDTO;
import com.padrino.armando.services.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestión de ventas
 */
@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;

    /**
     * Registrar una nueva venta
     */
    @PostMapping
    public ResponseEntity<VentaDTO> registrarVenta(@Valid @RequestBody VentaDTO ventaDTO) {
        log.info("POST /api/ventas - Registrar venta");
        VentaDTO nuevaVenta = ventaService.registrarVenta(ventaDTO);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    /**
     * Obtener ventas del día actual
     */
    @GetMapping("/hoy")
    public ResponseEntity<List<VentaDTO>> obtenerVentasDelDia() {
        log.info("GET /api/ventas/hoy - Obtener ventas del día");
        List<VentaDTO> ventas = ventaService.obtenerVentasDelDia();
        return ResponseEntity.ok(ventas);
    }

    /**
     * Obtener ventas por fecha específica
     */
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<VentaDTO>> obtenerVentasPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        log.info("GET /api/ventas/fecha/{} - Obtener ventas por fecha", fecha);
        List<VentaDTO> ventas = ventaService.obtenerVentasPorFecha(fecha);
        return ResponseEntity.ok(ventas);
    }

    /**
     * Obtener ventas por rango de fechas
     */
    @GetMapping("/rango")
    public ResponseEntity<List<VentaDTO>> obtenerVentasPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        log.info("GET /api/ventas/rango - Desde {} hasta {}", inicio, fin);
        List<VentaDTO> ventas = ventaService.obtenerVentasPorRango(inicio, fin);
        return ResponseEntity.ok(ventas);
    }

    /**
     * Obtener venta por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerVentaPorId(@PathVariable Long id) {
        log.info("GET /api/ventas/{} - Obtener venta por ID", id);
        VentaDTO venta = ventaService.obtenerVentaPorId(id);
        return ResponseEntity.ok(venta);
    }
}
