package com.cavosh.api_cafe.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDireccionResponseDTO {
    private Long id;
    private String etiqueta;
    private String direccion;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private boolean esPredeterminada;
}