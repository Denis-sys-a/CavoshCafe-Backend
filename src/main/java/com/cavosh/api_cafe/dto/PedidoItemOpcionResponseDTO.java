package com.cavosh.api_cafe.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemOpcionResponseDTO {
    private String nombreOpcion;
    private String nombreValor;
    private BigDecimal modificadorPrecio;
}
