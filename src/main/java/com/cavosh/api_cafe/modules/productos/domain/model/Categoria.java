package com.cavosh.api_cafe.modules.productos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    private Long id;
    private String nombre;
    private String descripcion;
}