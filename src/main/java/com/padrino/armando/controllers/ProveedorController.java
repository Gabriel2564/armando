package com.padrino.armando.controllers;

import com.padrino.armando.entities.Proveedor;
import com.padrino.armando.repositories.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorRepository proveedorRepository;

    @GetMapping
    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<Proveedor> buscarPorRuc(@PathVariable String ruc) {
        return proveedorRepository.findByRuc(ruc)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}