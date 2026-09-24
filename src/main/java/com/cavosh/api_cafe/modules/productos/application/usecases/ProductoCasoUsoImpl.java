package com.cavosh.api_cafe.modules.productos.application.usecases;

import com.cavosh.api_cafe.modules.productos.domain.exception.ResourceNotFoundException;
import com.cavosh.api_cafe.modules.productos.domain.model.Categoria;
import com.cavosh.api_cafe.modules.productos.domain.model.Producto;
import com.cavosh.api_cafe.modules.productos.domain.ports.in.CategoriaCasoUso;
import com.cavosh.api_cafe.modules.productos.domain.ports.in.ProductoCasoUso;
import com.cavosh.api_cafe.modules.productos.domain.ports.out.ProductoRepositorioPuerto;
import com.cavosh.api_cafe.modules.productos.domain.ports.out.ProductoSucursalStockRepositorioPuerto;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.dtos.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductoCasoUsoImpl implements ProductoCasoUso, CategoriaCasoUso {

    private final ProductoRepositorioPuerto productoRepositorioPuerto;
    private final ProductoSucursalStockRepositorioPuerto stockRepositorioPuerto;

    @Override
    public List<Producto> obtenerTodos() {
        return productoRepositorioPuerto.obtenerTodos();
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepositorioPuerto.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public Producto crear(ProductDTO dto) {
        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .imagenUrl(dto.getImagenUrl())
                .disponible(dto.getDisponible())
                .categoria(categoriaDesdeId(dto.getCategoriaId()))
                .build();
        return productoRepositorioPuerto.guardar(producto);
    }

    @Override
    public Producto actualizar(Long id, ProductDTO dto) {
        Producto productoExistente = obtenerPorId(id);
        productoExistente.setNombre(dto.getNombre());
        productoExistente.setDescripcion(dto.getDescripcion());
        productoExistente.setPrecio(dto.getPrecio());
        productoExistente.setImagenUrl(dto.getImagenUrl());
        productoExistente.setDisponible(dto.getDisponible());
        productoExistente.setCategoria(categoriaDesdeId(dto.getCategoriaId()));
        return productoRepositorioPuerto.guardar(productoExistente);
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        productoRepositorioPuerto.eliminar(id);
    }

    @Override
    public List<Categoria> obtenerTodas() {
        return productoRepositorioPuerto.obtenerTodasCategorias();
    }

    @Override
    public List<Producto> obtenerDisponiblesPorSucursal(Long sucursalId) {
        // Un producto sin fila en producto_sucursal_stock se considera disponible
        // por defecto; solo se descartan los marcados explícitamente como no disponibles.
        Set<Long> noDisponiblesEnSucursal =
                Set.copyOf(stockRepositorioPuerto.obtenerIdsNoDisponiblesPorSucursal(sucursalId));

        return obtenerTodos().stream()
                .filter(producto -> !noDisponiblesEnSucursal.contains(producto.getId()))
                .toList();
    }

    /** El adaptador de persistencia resuelve y valida el id (404 si la categoría no existe). */
    private Categoria categoriaDesdeId(Long categoriaId) {
        return Categoria.builder().id(categoriaId).build();
    }
}
