package com.padrino.armando.controllers;

import com.padrino.armando.dtos.orden.OrdenServicioRequestDTO;
import com.padrino.armando.dtos.orden.OrdenServicioResponseDTO;
import com.padrino.armando.services.OrdenServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
@Tag(name = "Órdenes de Servicio", description = "Registro de salidas / órdenes de servicio")
public class OrdenServicioController {

    private final OrdenServicioService ordenServicioService;

    @GetMapping
    @Operation(summary = "Listar todas las órdenes de servicio")
    public ResponseEntity<List<OrdenServicioResponseDTO>> listarTodas() {
        return ResponseEntity.ok(ordenServicioService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden de servicio por ID")
    public ResponseEntity<OrdenServicioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenServicioService.obtenerPorId(id));
    }

    @GetMapping("/buscar/cliente")
    @Operation(summary = "Buscar órdenes por cliente")
    public ResponseEntity<List<OrdenServicioResponseDTO>> buscarPorCliente(@RequestParam String cliente) {
        return ResponseEntity.ok(ordenServicioService.buscarPorCliente(cliente));
    }

    @GetMapping("/buscar/placa")
    @Operation(summary = "Buscar órdenes por placa")
    public ResponseEntity<List<OrdenServicioResponseDTO>> buscarPorPlaca(@RequestParam String placa) {
        return ResponseEntity.ok(ordenServicioService.buscarPorPlaca(placa));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva orden de servicio")
    public ResponseEntity<OrdenServicioResponseDTO> registrar(@Valid @RequestBody OrdenServicioRequestDTO dto) {
        return new ResponseEntity<>(ordenServicioService.registrar(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar orden de servicio")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenServicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}