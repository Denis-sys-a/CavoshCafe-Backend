package com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.in.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalResponseDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String horarioAtencion;
    private Boolean activa;
}