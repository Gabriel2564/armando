package com.padrino.armando.controllers;

import com.padrino.armando.dtos.llanta.LlantaRequestDTO;
import com.padrino.armando.dtos.llanta.LlantaResponseDTO;
import com.padrino.armando.services.LlantaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/llantas")
@RequiredArgsConstructor
@Tag(name = "Llantas", description = "Gestión del maestro de llantas")
public class LlantaController {

    private final LlantaService llantaService;

    @GetMapping
    @Operation(summary = "Listar todas las llantas con stock")
    public ResponseEntity<List<LlantaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(llantaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener llanta por ID")
    public ResponseEntity<LlantaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(llantaService.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar llantas por código, producto o medida")
    public ResponseEntity<List<LlantaResponseDTO>> buscar(@RequestParam String filtro) {
        return ResponseEntity.ok(llantaService.buscar(filtro));
    }

    @PostMapping
    @Operation(summary = "Crear nueva llanta")
    public ResponseEntity<LlantaResponseDTO> crear(@Valid @RequestBody LlantaRequestDTO dto) {
        return new ResponseEntity<>(llantaService.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar llanta")
    public ResponseEntity<LlantaResponseDTO> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody LlantaRequestDTO dto) {
        return ResponseEntity.ok(llantaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar llanta (soft delete)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        llantaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}