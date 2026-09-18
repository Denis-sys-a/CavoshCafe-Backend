package com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

import com.cavosh.api_cafe.dto.CarritoItemResponseDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoResponseDTO {
    private Long id;
    private Long usuarioId;
    private List<CarritoItemResponseDTO> items;
    private BigDecimal total;
}
