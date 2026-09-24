package com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.out.persistence.entities;

import com.cavosh.api_cafe.modules.pedidos.domain.enums.EstadoPedido;
import com.cavosh.api_cafe.modules.pedidos.domain.enums.MetodoEntrega;
import com.cavosh.api_cafe.modules.promociones.infrastructure.adapters.out.persistence.entities.CodigoPromocionalEntity;
import com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.out.persistence.entities.SucursalEntity;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.entities.DireccionEntity;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.entities.UsuarioEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tabla {@code pedidos}.
 *
 * Equivalencias con el dominio {@code Pedido}:
 * - numero_pedido: identificador legible y único; solo existe en BD/dominio
 * como dato asignado al crear.
 * - subtotal / descuento / total: el dominio solo expone {@code total};
 * subtotal y descuento
 * (NOT NULL en BD) los deriva el mapper a partir de los detalles.
 * - created_at <-> fechaCreacion.
 * - hora_recojo y la tabla {@code pagos}: fuera del alcance de esta entidad (el
 * dominio no las modela aquí).
 *
 * Se usa @Getter/@Setter (y no @Data) a propósito para evitar
 * equals/hashCode/toString
 * recursivos con las relaciones bidireccionales.
 */
@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_pedido", nullable = false, unique = true, updatable = false, length = 20)
    private String numeroPedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private SucursalEntity sucursal;

    /**
     * Obligatoria solo cuando {@code metodoEntrega = DELIVERY} (lo garantiza un
     * CHECK en BD).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_id")
    private DireccionEntity direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_promocional_id")
    private CodigoPromocionalEntity codigoPromocional;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_entrega", nullable = false, length = 20)
    private MetodoEntrega metodoEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPedido estado = EstadoPedido.CREADO;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Ítems del pedido. El pedido es el agregado raíz: los detalles se guardan y
     * eliminan
     * en cascada con él. Se cargan por lotes para evitar el problema N+1 al listar
     * pedidos.
     */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    @BatchSize(size = 50)
    private List<DetallePedidoEntity> detalles = new ArrayList<>();

    public void agregarDetalle(DetallePedidoEntity detalle) {
        detalle.setPedido(this);
        detalles.add(detalle);
    }

    /**
     * Reemplaza los detalles conservando la misma colección (requisito de
     * orphanRemoval):
     * los que no vuelvan a agregarse se eliminan al hacer flush.
     */
    public void reemplazarDetalles(List<DetallePedidoEntity> nuevos) {
        detalles.clear();
        nuevos.forEach(this::agregarDetalle);
    }
}
