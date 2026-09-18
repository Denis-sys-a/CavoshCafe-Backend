package com.cavosh.api_cafe.modules.productos.domain.ports.in;

import com.cavosh.api_cafe.modules.productos.domain.model.Producto;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.dtos.ProductDTO;

import java.util.List;

public interface ProductoCasoUso {
    List<Producto> obtenerTodos();
    Producto obtenerPorId(Long id);
    Producto crear(ProductDTO dto);
    Producto actualizar(Long id, ProductDTO dto);
    void eliminar(Long id);
}