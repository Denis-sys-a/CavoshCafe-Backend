package com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Tabla {@code pedido_items} (detalle de un pedido).
 *
 * Es una "foto" del producto al momento de la compra: se guarda
 * {@code nombre_producto} y
 * {@code precio_unitario} para que el historial no cambie si el producto se
 * edita o se
 * elimina. Por eso el producto se referencia por ID plano (nullable: la FK es
 * ON DELETE SET NULL) y no como relación a {@code ProductEntity}.
 *
 * Las personalizaciones del ítem ({@code pedido_item_opciones}) no se mapean
 * todavía.
 */
@Entity
@Table(name = "pedido_items")
@Getter
@Setter
@NoArgsConstructor
public class DetallePedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;

    @Column(name = "producto_id")
    private Long productoId;

    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombreProducto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 1;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}
