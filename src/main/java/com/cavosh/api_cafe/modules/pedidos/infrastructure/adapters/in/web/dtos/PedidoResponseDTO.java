package com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.in.web.dtos;

import com.cavosh.api_cafe.modules.pedidos.domain.enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    private Long id;
    private Long usuarioId;
    private EstadoPedido estado;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
}