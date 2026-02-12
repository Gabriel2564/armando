package com.padrino.armando.controllers;

import com.padrino.armando.dtos.entrada.EntradaResponseDTO;
import com.padrino.armando.dtos.entrada.EntradaServicioRequestDTO;
import com.padrino.armando.services.EntradaServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entradas/servicios")
@RequiredArgsConstructor
@Tag(name = "Entradas Servicios", description = "Registro de compras/entradas de insumos de servicio")
public class EntradaServicioController {

    private final EntradaServicioService entradaServicioService;

    @GetMapping
    @Operation(summary = "Listar todas las entradas de servicios")
    public ResponseEntity<List<EntradaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(entradaServicioService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener entrada de servicio por ID")
    public ResponseEntity<EntradaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(entradaServicioService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva entrada/compra de insumos de servicio")
    public ResponseEntity<EntradaResponseDTO> registrar(@Valid @RequestBody EntradaServicioRequestDTO dto) {
        return new ResponseEntity<>(entradaServicioService.registrar(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar entrada de servicio")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        entradaServicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}