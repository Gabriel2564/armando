package com.padrino.armando.controllers;

import com.padrino.armando.dtos.venta.VentaLlantaRequestDTO;
import com.padrino.armando.dtos.venta.VentaLlantaResponseDTO;
import com.padrino.armando.services.VentaLlantaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas/llantas")
@RequiredArgsConstructor
@Tag(name = "Ventas Llantas", description = "Registro de ventas directas de llantas")
public class VentaLlantaController {

    private final VentaLlantaService ventaLlantaService;

    @GetMapping
    @Operation(summary = "Listar todas las ventas de llantas")
    public ResponseEntity<List<VentaLlantaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(ventaLlantaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta por ID")
    public ResponseEntity<VentaLlantaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaLlantaService.obtenerPorId(id));
    }

    @GetMapping("/buscar/cliente")
    @Operation(summary = "Buscar ventas por cliente")
    public ResponseEntity<List<VentaLlantaResponseDTO>> buscarPorCliente(@RequestParam String cliente) {
        return ResponseEntity.ok(ventaLlantaService.buscarPorCliente(cliente));
    }

    @GetMapping("/buscar/placa")
    @Operation(summary = "Buscar ventas por placa")
    public ResponseEntity<List<VentaLlantaResponseDTO>> buscarPorPlaca(@RequestParam String placa) {
        return ResponseEntity.ok(ventaLlantaService.buscarPorPlaca(placa));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva venta de llantas")
    public ResponseEntity<VentaLlantaResponseDTO> registrar(@Valid @RequestBody VentaLlantaRequestDTO dto) {
        return new ResponseEntity<>(ventaLlantaService.registrar(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta de llantas")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaLlantaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}