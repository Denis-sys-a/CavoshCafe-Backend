package com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.in.web.dtos;

import com.cavosh.api_cafe.entity.MetodoEntrega;
import com.cavosh.api_cafe.modules.pedidos.domain.enums.EstadoPedido;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDTO {
    private Long id;
    private String numeroPedido;
    private Long usuarioId;
    private Long sucursalId;
    private EstadoPedido estado;
    private MetodoEntrega metodoEntrega;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private List<PedidoItemResponseDTO> items;
    private LocalDateTime createdAt;
}
