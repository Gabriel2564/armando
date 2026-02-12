package com.padrino.armando.mappers;

import com.padrino.armando.dtos.llanta.LlantaRequestDTO;
import com.padrino.armando.dtos.llanta.LlantaResponseDTO;
import com.padrino.armando.entities.Llanta;
import org.springframework.stereotype.Component;

@Component
public class LlantaMapper {

    public Llanta toEntity(LlantaRequestDTO dto) {
        return Llanta.builder()
                .codigo(dto.getCodigo())
                .producto(dto.getProducto())
                .medida(dto.getMedida())
                .tipo(dto.getTipo())
                .proveedor(dto.getProveedor())
                .activo(true)
                .build();
    }

    public LlantaResponseDTO toResponseDTO(Llanta llanta, Integer entradas, Integer salidas) {
        return LlantaResponseDTO.builder()
                .id(llanta.getId())
                .codigo(llanta.getCodigo())
                .producto(llanta.getProducto())
                .medida(llanta.getMedida())
                .tipo(llanta.getTipo())
                .proveedor(llanta.getProveedor())
                .entradas(entradas)
                .salidas(salidas)
                .stock(entradas - salidas)
                .build();
    }

    public void updateEntity(Llanta llanta, LlantaRequestDTO dto) {
        llanta.setCodigo(dto.getCodigo());
        llanta.setProducto(dto.getProducto());
        llanta.setMedida(dto.getMedida());
        llanta.setTipo(dto.getTipo());
        llanta.setProveedor(dto.getProveedor());
    }
}