package com.cavosh.api_cafe.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

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
