package com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Nota: no usa @ManyToOne hacia ProductEntity porque esa entidad JPA todavía
 * no está implementada en el proyecto (es una clase vacía); se referencia el
 * producto por ID plano.
 */
@Entity
@Table(name = "carrito_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private CarritoEntity carrito;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "carritoItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CarritoItemOpcionEntity> opciones = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Conjunto (orden-independiente) de IDs de producto_opcion_valores elegidos
     * para este ítem. Se usa para comparar si dos solicitudes de "agregar al
     * carrito" representan exactamente la misma personalización.
     */
    public Set<Long> opcionesValoresIds() {
        return opciones.stream()
                .map(CarritoItemOpcionEntity::getProductoOpcionValorId)
                .collect(Collectors.toSet());
    }
}
