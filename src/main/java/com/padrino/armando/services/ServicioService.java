package com.padrino.armando.services;

import com.padrino.armando.dtos.servicio.ServicioRequestDTO;
import com.padrino.armando.dtos.servicio.ServicioResponseDTO;
import com.padrino.armando.entities.Servicio;
import com.padrino.armando.exceptions.DuplicateResourceException;
import com.padrino.armando.exceptions.ResourceNotFoundException;
import com.padrino.armando.mappers.ServicioMapper;
import com.padrino.armando.repositories.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;

    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> listarTodos() {
        return servicioRepository.findByActivoTrue().stream()
                .map(this::buildResponseWithStock)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServicioResponseDTO obtenerPorId(Long id) {
        Servicio servicio = findServicioOrThrow(id);
        return buildResponseWithStock(servicio);
    }

    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> buscar(String filtro) {
        return servicioRepository.buscarPorFiltro(filtro).stream()
                .map(this::buildResponseWithStock)
                .toList();
    }

    @Transactional
    public ServicioResponseDTO crear(ServicioRequestDTO dto) {
        if (servicioRepository.existsByCodigo(dto.getCodigo())) {
            throw new DuplicateResourceException("Ya existe un servicio con código: " + dto.getCodigo());
        }
        Servicio servicio = servicioMapper.toEntity(dto);
        servicio = servicioRepository.save(servicio);
        return buildResponseWithStock(servicio);
    }

    @Transactional
    public ServicioResponseDTO actualizar(Long id, ServicioRequestDTO dto) {
        Servicio servicio = findServicioOrThrow(id);

        servicioRepository.findByCodigo(dto.getCodigo())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Ya existe otro servicio con código: " + dto.getCodigo());
                });

        servicioMapper.updateEntity(servicio, dto);
        servicio = servicioRepository.save(servicio);
        return buildResponseWithStock(servicio);
    }

    @Transactional
    public void eliminar(Long id) {
        Servicio servicio = findServicioOrThrow(id);
        servicio.setActivo(false);
        servicioRepository.save(servicio);
    }

    public Servicio findServicioOrThrow(Long id) {
        return servicioRepository.findById(id)
                .filter(Servicio::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));
    }

    private ServicioResponseDTO buildResponseWithStock(Servicio servicio) {
        Integer entradas = servicioRepository.calcularTotalEntradas(servicio.getId());
        Integer salidas = servicioRepository.calcularTotalSalidas(servicio.getId());
        return servicioMapper.toResponseDTO(servicio, entradas, salidas);
    }
}