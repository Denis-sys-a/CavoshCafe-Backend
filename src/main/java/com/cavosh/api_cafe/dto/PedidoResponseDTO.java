package com.cavosh.api_cafe.dto;

import com.cavosh.api_cafe.entity.EstadoPedido;
import com.cavosh.api_cafe.entity.MetodoEntrega;
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
