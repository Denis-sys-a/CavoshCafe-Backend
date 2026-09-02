package com.cavosh.api_cafe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDireccionRequestDTO {

    @NotBlank(message = "La etiqueta es obligatoria")
    @Size(max = 50)
    private String etiqueta;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private BigDecimal latitud;

    private BigDecimal longitud;

    private boolean esPredeterminada;
}