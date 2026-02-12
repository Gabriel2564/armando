package com.padrino.armando.controllers;

import com.padrino.armando.dtos.servicio.ServicioRequestDTO;
import com.padrino.armando.dtos.servicio.ServicioResponseDTO;
import com.padrino.armando.services.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
@Tag(name = "Servicios", description = "Gestión del maestro de productos de servicio")
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping
    @Operation(summary = "Listar todos los servicios con stock")
    public ResponseEntity<List<ServicioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener servicio por ID")
    public ResponseEntity<ServicioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar servicios por código o producto")
    public ResponseEntity<List<ServicioResponseDTO>> buscar(@RequestParam String filtro) {
        return ResponseEntity.ok(servicioService.buscar(filtro));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo servicio")
    public ResponseEntity<ServicioResponseDTO> crear(@Valid @RequestBody ServicioRequestDTO dto) {
        return new ResponseEntity<>(servicioService.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar servicio")
    public ResponseEntity<ServicioResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ServicioRequestDTO dto) {
        return ResponseEntity.ok(servicioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar servicio (soft delete)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}