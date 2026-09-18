package com.cavosh.api_cafe.modules.productos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagenUrl;
    private Boolean disponible;
    private Categoria categoria;

    @Builder.Default
    private List<OpcionProducto> opciones = new ArrayList<>();
}