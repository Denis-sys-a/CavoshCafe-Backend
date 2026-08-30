package com.cavosh.api_cafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "producto_opcion_valores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoOpcionValor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_opcion_id", nullable = false)
    private ProductoOpcion productoOpcion;

    @Column(name = "nombre_valor", nullable = false, length = 50)
    private String nombreValor;

    @Column(name = "modificador_precio", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal modificadorPrecio = BigDecimal.ZERO;

    @Column(name = "es_predeterminado", nullable = false)
    @Builder.Default
    private boolean esPredeterminado = false;
}
