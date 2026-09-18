package com.cavosh.api_cafe.modules.pedidos.domain.ports.in;

import com.cavosh.api_cafe.modules.pedidos.domain.model.Pedido;
import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.in.web.dtos.CrearPedidoRequestDTO;

public interface CrearPedidoCasoUso {
    Pedido crearPedido(CrearPedidoRequestDTO dto);
}