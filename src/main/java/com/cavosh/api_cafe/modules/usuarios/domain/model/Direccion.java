package com.cavosh.api_cafe.modules.usuarios.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    private Long id;
    private String calle;
    private String ciudad;
    private String distrito;
    private String referencia;
    private Boolean esPrincipal;
}