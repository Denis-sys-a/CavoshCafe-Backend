package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DireccionRequestDTO {

    @Size(max = 50, message = "La etiqueta no puede superar los 50 caracteres")
    private String etiqueta;

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 120, message = "La calle no puede superar los 120 caracteres")
    private String calle;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 60, message = "La ciudad no puede superar los 60 caracteres")
    private String ciudad;

    @Size(max = 60, message = "El distrito no puede superar los 60 caracteres")
    private String distrito;
    @Size(max = 255, message = "La referencia no puede superar los 255 caracteres")
    private String referencia;
    private Boolean esPrincipal;
}