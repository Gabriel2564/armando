package com.padrino.armando.controllers;

import com.padrino.armando.dtos.costo.KardexItemDTO;
import com.padrino.armando.dtos.costo.ResumenCostoDTO;
import com.padrino.armando.services.CostoPromedioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/costos")
@RequiredArgsConstructor
@Tag(name = "Costos", description = "Consulta de costo promedio y Kardex de productos")
public class CostoController {

    private final CostoPromedioService costoPromedioService;

    @GetMapping("/kardex/{llantaId}")
    @Operation(summary = "Obtener Kardex completo de una llanta (costo promedio móvil)")
    public ResponseEntity<List<KardexItemDTO>> obtenerKardex(@PathVariable Long llantaId) {
        return ResponseEntity.ok(costoPromedioService.construirKardex(llantaId));
    }

    @GetMapping("/resumen/{llantaId}")
    @Operation(summary = "Obtener resumen de costo promedio actual de una llanta")
    public ResponseEntity<ResumenCostoDTO> obtenerResumen(@PathVariable Long llantaId) {
        return ResponseEntity.ok(costoPromedioService.obtenerResumenCosto(llantaId));
    }
}