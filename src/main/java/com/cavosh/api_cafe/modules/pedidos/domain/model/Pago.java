package com.cavosh.api_cafe.modules.pedidos.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.cavosh.api_cafe.entity.EstadoPago;
import com.cavosh.api_cafe.entity.MetodoPago;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "ultimos_cuatro_tarjeta", length = 4)
    private String ultimosCuatroTarjeta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @Column(name = "pagado_en")
    private LocalDateTime pagadoEn;
}
