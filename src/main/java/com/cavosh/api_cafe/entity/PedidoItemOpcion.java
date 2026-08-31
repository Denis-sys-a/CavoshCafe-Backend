package com.cavosh.api_cafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pedido_item_opciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemOpcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_item_id", nullable = false)
    private PedidoItem pedidoItem;

    @Column(name = "nombre_opcion", nullable = false, length = 50)
    private String nombreOpcion;

    @Column(name = "nombre_valor", nullable = false, length = 50)
    private String nombreValor;

    @Column(name = "modificador_precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal modificadorPrecio;
}
