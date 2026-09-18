package com.cavosh.api_cafe.modules.pedidos.domain.ports.in;

import com.cavosh.api_cafe.modules.pedidos.domain.model.Pedido;

import java.util.List;

public interface ConsultarPedidoCasoUso {
    Pedido obtenerPorId(Long id);
    List<Pedido> obtenerPorUsuario(Long usuarioId);
}