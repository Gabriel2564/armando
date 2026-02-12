package com.padrino.armando.controllers;

import com.padrino.armando.dtos.entrada.EntradaLlantaRequestDTO;
import com.padrino.armando.dtos.entrada.EntradaResponseDTO;
import com.padrino.armando.services.EntradaLlantaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entradas/llantas")
@RequiredArgsConstructor
@Tag(name = "Entradas Llantas", description = "Registro de compras/entradas de llantas")
public class EntradaLlantaController {

    private final EntradaLlantaService entradaLlantaService;

    @GetMapping
    @Operation(summary = "Listar todas las entradas de llantas")
    public ResponseEntity<List<EntradaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(entradaLlantaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener entrada de llanta por ID")
    public ResponseEntity<EntradaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(entradaLlantaService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva entrada/compra de llantas")
    public ResponseEntity<EntradaResponseDTO> registrar(@Valid @RequestBody EntradaLlantaRequestDTO dto) {
        return new ResponseEntity<>(entradaLlantaService.registrar(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar entrada de llanta")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        entradaLlantaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}