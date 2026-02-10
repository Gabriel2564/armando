package com.padrino.armando.controllers;

import com.padrino.armando.dtos.VentaDTO;
import com.padrino.armando.services.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaDTO> registrarVenta(@Valid @RequestBody VentaDTO ventaDTO) {
        log.info("POST /api/ventas - Registrar nueva venta");
        return new ResponseEntity<>(ventaService.registrarVenta(ventaDTO), HttpStatus.CREATED);
    }

    @GetMapping("/hoy")
    public ResponseEntity<List<VentaDTO>> obtenerVentasDelDia() {
        log.info("GET /api/ventas/hoy - Obtener ventas del día");
        return ResponseEntity.ok(ventaService.obtenerVentasDelDia());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/ventas/{} - Obtener venta por ID", id);
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }
}