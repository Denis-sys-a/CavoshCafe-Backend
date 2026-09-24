package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.adapters;

import com.cavosh.api_cafe.modules.productos.domain.exception.ResourceNotFoundException;
import com.cavosh.api_cafe.modules.productos.domain.model.Categoria;
import com.cavosh.api_cafe.modules.productos.domain.model.OpcionProducto;
import com.cavosh.api_cafe.modules.productos.domain.model.Producto;
import com.cavosh.api_cafe.modules.productos.domain.ports.out.ProductoRepositorioPuerto;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities.ProductCategoryEntity;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities.ProductEntity;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities.ProductoOpcionEntity;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.repository.SpringDataProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida (persistencia) del módulo de productos. Los métodos son
 * transaccionales para poder recorrer las colecciones LAZY (opciones/valores)
 * al mapear a dominio, sin depender de open-in-view.
 */
@Component
@RequiredArgsConstructor
public class ProductoPersistenceAdapter implements ProductoRepositorioPuerto {

    private final SpringDataProductRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        return repository.findAll(Sort.by("id")).stream()
                .map(this::aDominio)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerPorId(Long id) {
        return repository.findById(id).map(this::aDominio);
    }

    /**
     * Inserta (id nulo) o actualiza (id existente). Al actualizar se parte de la
     * entidad ya persistida para no perder columnas que el dominio no modela
     * (created_at) ni las opciones del producto.
     */
    @Override
    @Transactional
    public Producto guardar(Producto producto) {
        ProductEntity entity = producto.getId() == null
                ? new ProductEntity()
                : repository.findById(producto.getId()).orElseGet(ProductEntity::new);
        aplicarDominio(entity, producto);
        return aDominio(repository.save(entity));
    }

    /** Borrado físico. Falla si el producto está referenciado (carrito_items, pedido_items). */
    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Categoria> obtenerTodasCategorias() {
        return Arrays.stream(ProductCategoryEntity.values())
                .map(this::categoriaADominio)
                .toList();
    }

    // ------------------------------------------------------------------
    // Mapeo dominio <-> entidad
    // ------------------------------------------------------------------

    private void aplicarDominio(ProductEntity entity, Producto modelo) {
        entity.setNombre(modelo.getNombre());
        entity.setDescripcion(modelo.getDescripcion());
        entity.setPrecioBase(modelo.getPrecio());
        entity.setUrlImagen(modelo.getImagenUrl());
        // disponible (dominio) <-> activo (BD). Nulo = conservar el valor actual
        // (una entidad nueva arranca en activo = true).
        if (modelo.getDisponible() != null) {
            entity.setActivo(modelo.getDisponible());
        }
        if (modelo.getCategoria() != null && modelo.getCategoria().getId() != null) {
            Long categoriaId = modelo.getCategoria().getId();
            ProductCategoryEntity categoria = ProductCategoryEntity.desdeId(categoriaId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría no encontrada con ID: " + categoriaId));
            entity.setCategoria(categoria);
            entity.setTipoProducto(categoria.getTipoProducto());
        }
        if (entity.getCategoria() == null) {
            throw new IllegalArgumentException("El producto requiere una categoría");
        }
    }

    private Producto aDominio(ProductEntity entity) {
        return Producto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .precio(entity.getPrecioBase())
                .imagenUrl(entity.getUrlImagen())
                .disponible(entity.isActivo())
                .categoria(categoriaADominio(entity.getCategoria()))
                .opciones(opcionesADominio(entity))
                .build();
    }

    private Categoria categoriaADominio(ProductCategoryEntity categoria) {
        if (categoria == null) {
            return null;
        }
        return Categoria.builder()
                .id(categoria.getId())
                .nombre(categoria.name())
                .descripcion(categoria.getDescripcion())
                .build();
    }

    /**
     * Aplana grupo -> valores: cada valor de producto_opcion_valores es una
     * OpcionProducto cuyo id es el que se envía en opcionesValoresIds del carrito.
     */
    private List<OpcionProducto> opcionesADominio(ProductEntity entity) {
        return entity.getOpciones().stream()
                .map(ProductoOpcionEntity::getValores)
                .flatMap(List::stream)
                .map(valor -> OpcionProducto.builder()
                        .id(valor.getId())
                        .nombre(valor.getNombreValor())
                        .precioAdicional(valor.getModificadorPrecio())
                        .build())
                .toList();
    }
}
