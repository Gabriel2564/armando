package com.padrino.armando.services;

import com.padrino.armando.dtos.costo.ResumenCostoDTO;
import com.padrino.armando.dtos.llanta.LlantaRequestDTO;
import com.padrino.armando.dtos.llanta.LlantaResponseDTO;
import com.padrino.armando.entities.Llanta;
import com.padrino.armando.exceptions.DuplicateResourceException;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.mappers.LlantaMapper;
import com.padrino.armando.repositories.LlantaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LlantaService {

    private final LlantaRepository llantaRepository;
    private final LlantaMapper llantaMapper;
    @Lazy
    private final CostoPromedioService costoPromedioService;

    @Transactional(readOnly = true)
    public List<LlantaResponseDTO> listarTodas() {
        return llantaRepository.findByActivoTrue().stream()
                .map(this::buildResponseWithStock)
                .toList();
    }

    @Transactional(readOnly = true)
    public LlantaResponseDTO obtenerPorId(Long id) {
        Llanta llanta = findLlantaOrThrow(id);
        return buildResponseWithStock(llanta);
    }

    @Transactional(readOnly = true)
    public List<LlantaResponseDTO> buscar(String filtro) {
        return llantaRepository.buscarPorFiltro(filtro).stream()
                .map(this::buildResponseWithStock)
                .toList();
    }

    @Transactional
    public LlantaResponseDTO crear(LlantaRequestDTO dto) {
        if (llantaRepository.existsByCodigo(dto.getCodigo())) {
            throw new DuplicateResourceException("Ya existe una llanta con código: " + dto.getCodigo());
        }
        Llanta llanta = llantaMapper.toEntity(dto);
        llanta = llantaRepository.save(llanta);
        return buildResponseWithStock(llanta);
    }

    @Transactional
    public LlantaResponseDTO actualizar(Long id, LlantaRequestDTO dto) {
        Llanta llanta = findLlantaOrThrow(id);
        llantaRepository.findByCodigo(dto.getCodigo())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Ya existe otra llanta con código: " + dto.getCodigo());
                });
        llantaMapper.updateEntity(llanta, dto);
        llanta = llantaRepository.save(llanta);
        return buildResponseWithStock(llanta);
    }

    @Transactional
    public void eliminar(Long id) {
        Llanta llanta = findLlantaOrThrow(id);
        llanta.setActivo(false);
        llantaRepository.save(llanta);
    }

    public Llanta findLlantaOrThrow(Long id) {
        return llantaRepository.findById(id)
                .filter(Llanta::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Llanta no encontrada con ID: " + id));
    }

    private LlantaResponseDTO buildResponseWithStock(Llanta llanta) {
        Integer entradas = llantaRepository.calcularTotalEntradas(llanta.getId());
        Integer salidas = llantaRepository.calcularTotalSalidas(llanta.getId());

        LlantaResponseDTO response = llantaMapper.toResponseDTO(llanta, entradas, salidas);

        // Agregar info de costo promedio
        try {
            ResumenCostoDTO resumen = costoPromedioService.obtenerResumenCosto(llanta.getId());
            response.setCostoPromedio(resumen.getCostoPromedio());
            response.setValorInventario(resumen.getValorInventario());
        } catch (Exception e) {
            // Si no hay movimientos, dejar en null
        }

        return response;
    }
}