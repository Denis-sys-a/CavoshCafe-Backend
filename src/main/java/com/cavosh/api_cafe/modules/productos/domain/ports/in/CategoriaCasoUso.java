package com.cavosh.api_cafe.modules.productos.domain.ports.in;

import com.cavosh.api_cafe.modules.productos.domain.model.Categoria;

import java.util.List;

public interface CategoriaCasoUso {
    List<Categoria> obtenerTodas();
}