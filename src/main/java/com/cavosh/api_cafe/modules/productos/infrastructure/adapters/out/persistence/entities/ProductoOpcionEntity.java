package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Tabla {@code producto_opciones}: un grupo de personalización de un producto
 * (p. ej. "Tamaño", "Tipo de leche"). Sus valores están en producto_opcion_valores.
 */
@Entity
@Table(name = "producto_opciones")
@Getter
@Setter
@NoArgsConstructor
public class ProductoOpcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductEntity producto;

    @Column(name = "nombre_opcion", nullable = false, length = 50)
    private String nombreOpcion;

    /** 'UNICA' | 'MULTIPLE' */
    @Column(name = "tipo_seleccion", nullable = false, length = 20)
    private String tipoSeleccion = "UNICA";

    @Column(name = "es_obligatoria", nullable = false)
    private boolean obligatoria = false;

    @OneToMany(mappedBy = "opcion", fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    @BatchSize(size = 50)
    private List<ProductoOpcionValorEntity> valores = new ArrayList<>();
}
