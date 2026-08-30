package com.cavosh.api_cafe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto_opciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoOpcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Product producto;

    @Column(name = "nombre_opcion", nullable = false, length = 50)
    private String nombreOpcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_seleccion", nullable = false)
    private TipoSeleccion tipoSeleccion;

    @Column(name = "es_obligatoria", nullable = false)
    @Builder.Default
    private boolean esObligatoria = false;
}
