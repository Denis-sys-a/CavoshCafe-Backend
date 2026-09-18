package com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.in.web.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDTO {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección de la sucursal es obligatoria")
    private String direccion;

    private String telefono;
    private String horarioAtencion;
    private Boolean activa;
}