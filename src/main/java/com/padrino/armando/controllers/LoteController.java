package com.padrino.armando.controllers;

import com.padrino.armando.dtos.LoteDTO;
import com.padrino.armando.services.LoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lotes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class LoteController {

    private final LoteService loteService;

    @GetMapping
    public ResponseEntity<List<LoteDTO>> obtenerTodos() {
        log.info("GET /api/lotes - Obtener todos los lotes");
        return ResponseEntity.ok(loteService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<LoteDTO>> obtenerActivos() {
        log.info("GET /api/lotes/activos - Obtener lotes activos");
        return ResponseEntity.ok(loteService.obtenerActivos());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<LoteDTO>> obtenerPorTipo(@PathVariable String tipo) {
        log.info("GET /api/lotes/tipo/{} - Obtener lotes por tipo", tipo);
        return ResponseEntity.ok(loteService.obtenerPorTipo(tipo));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<LoteDTO>> obtenerPorProducto(@PathVariable Long productoId) {
        log.info("GET /api/lotes/producto/{} - Obtener lotes del producto", productoId);
        return ResponseEntity.ok(loteService.obtenerPorProducto(productoId));
    }

    @PostMapping
    public ResponseEntity<LoteDTO> crearNuevoLote(@Valid @RequestBody LoteDTO loteDTO) {
        log.info("POST /api/lotes - Crear nuevo lote");
        return new ResponseEntity<>(loteService.crearNuevoLote(loteDTO), HttpStatus.CREATED);
    }
}