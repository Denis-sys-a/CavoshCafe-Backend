package com.cavosh.api_cafe.modules.sucursales.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String horarioAtencion;
    private Boolean activa;
}