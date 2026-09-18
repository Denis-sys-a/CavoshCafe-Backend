package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagenUrl;
    private Boolean disponible;
    private String categoriaNombre;
}