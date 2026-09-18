package com.cavosh.api_cafe.modules.productos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpcionProducto {

    private Long id;
    private String nombre;
    private BigDecimal precioAdicional;
}