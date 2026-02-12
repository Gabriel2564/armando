package com.padrino.armando.mappers;

import com.padrino.armando.dtos.servicio.ServicioRequestDTO;
import com.padrino.armando.dtos.servicio.ServicioResponseDTO;
import com.padrino.armando.entities.Servicio;
import org.springframework.stereotype.Component;

@Component
public class ServicioMapper {

    public Servicio toEntity(ServicioRequestDTO dto) {
        return Servicio.builder()
                .codigo(dto.getCodigo())
                .producto(dto.getProducto())
                .tipo(dto.getTipo())
                .precio(dto.getPrecio())
                .activo(true)
                .build();
    }

    public ServicioResponseDTO toResponseDTO(Servicio servicio, Integer entradas, Integer salidas) {
        return ServicioResponseDTO.builder()
                .id(servicio.getId())
                .codigo(servicio.getCodigo())
                .producto(servicio.getProducto())
                .tipo(servicio.getTipo())
                .precio(servicio.getPrecio())
                .entradas(entradas)
                .salidas(salidas)
                .stock(entradas - salidas)
                .build();
    }

    public void updateEntity(Servicio servicio, ServicioRequestDTO dto) {
        servicio.setCodigo(dto.getCodigo());
        servicio.setProducto(dto.getProducto());
        servicio.setTipo(dto.getTipo());
        servicio.setPrecio(dto.getPrecio());
    }
}