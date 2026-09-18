package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.dtos;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String etiqueta;
    private String direccion;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private boolean esPredeterminada;
}