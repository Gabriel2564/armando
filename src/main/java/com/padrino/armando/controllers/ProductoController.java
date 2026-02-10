package com.padrino.armando.controllers;

import com.padrino.armando.dtos.ProductoDTO;
import com.padrino.armando.services.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodos() {
        log.info("GET /api/productos - Obtener todos los productos");
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/productos/{} - Obtener producto por ID", id);
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoDTO> obtenerPorCodigo(@PathVariable String codigo) {
        log.info("GET /api/productos/codigo/{} - Obtener producto por código", codigo);
        return ResponseEntity.ok(productoService.obtenerPorCodigo(codigo));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ProductoDTO>> obtenerPorTipo(@PathVariable String tipo) {
        log.info("GET /api/productos/tipo/{} - Obtener productos por tipo", tipo);
        return ResponseEntity.ok(productoService.obtenerPorTipo(tipo));
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> obtenerTipos() {
        log.info("GET /api/productos/tipos - Obtener todos los tipos");
        return ResponseEntity.ok(productoService.obtenerTipos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscar(@RequestParam String q) {
        log.info("GET /api/productos/buscar?q={} - Buscar productos", q);
        return ResponseEntity.ok(productoService.buscar(q));
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody ProductoDTO productoDTO) {
        log.info("POST /api/productos - Crear producto");
        return new ResponseEntity<>(productoService.crear(productoDTO), HttpStatus.CREATED);
    }
}