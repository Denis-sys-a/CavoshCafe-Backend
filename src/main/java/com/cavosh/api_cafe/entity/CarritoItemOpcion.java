package com.cavosh.api_cafe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carrito_item_opciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoItemOpcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_item_id", nullable = false)
    private CarritoItem carritoItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_opcion_valor_id", nullable = false)
    private ProductoOpcionValor productoOpcionValor;
}
