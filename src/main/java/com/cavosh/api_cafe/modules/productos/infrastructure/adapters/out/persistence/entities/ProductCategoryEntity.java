package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities;

import java.util.Arrays;
import java.util.Optional;

/**
 * Columna productos.categoria ENUM(...). En la BD no existe una tabla de
 * categorías: la categoría es un enum. Los IDs numéricos expuestos por la API
 * (Categoria.id / ProductDTO.categoriaId) son fijos y NO dependen del orden de
 * declaración, así que no cambies un ID existente; agrega nuevos al final.
 */
public enum ProductCategoryEntity {
    BEBIDAS_CALIENTES(1L, "Bebidas calientes", TipoProductoEntity.BEBIDA),
    BEBIDAS_FRIAS(2L, "Bebidas frías", TipoProductoEntity.BEBIDA),
    BASE_ESPRESSO(3L, "Base espresso", TipoProductoEntity.BEBIDA),
    SIN_CAFE(4L, "Sin café", TipoProductoEntity.BEBIDA),
    POSTRES(5L, "Postres", TipoProductoEntity.COMIDA);

    private final Long id;
    private final String descripcion;
    private final TipoProductoEntity tipoProducto;

    ProductCategoryEntity(Long id, String descripcion, TipoProductoEntity tipoProducto) {
        this.id = id;
        this.descripcion = descripcion;
        this.tipoProducto = tipoProducto;
    }

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /** Valor de productos.tipo_producto que corresponde a esta categoría. */
    public TipoProductoEntity getTipoProducto() {
        return tipoProducto;
    }

    public static Optional<ProductCategoryEntity> desdeId(Long id) {
        return Arrays.stream(values()).filter(c -> c.id.equals(id)).findFirst();
    }
}
