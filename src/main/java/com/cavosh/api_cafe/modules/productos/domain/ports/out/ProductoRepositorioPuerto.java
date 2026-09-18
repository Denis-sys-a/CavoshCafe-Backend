package com.cavosh.api_cafe.modules.productos.domain.ports.out;

import com.cavosh.api_cafe.modules.productos.domain.model.Categoria;
import com.cavosh.api_cafe.modules.productos.domain.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepositorioPuerto {
    List<Producto> obtenerTodos();
    Optional<Producto> obtenerPorId(Long id);
    Producto guardar(Producto producto);
    void eliminar(Long id);
    List<Categoria> obtenerTodasCategorias();
}