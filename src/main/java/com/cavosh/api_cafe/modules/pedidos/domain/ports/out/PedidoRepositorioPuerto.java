package com.cavosh.api_cafe.modules.pedidos.domain.ports.out;

import com.cavosh.api_cafe.modules.pedidos.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepositorioPuerto {
    Pedido guardar(Pedido pedido);
    Optional<Pedido> buscarPorId(Long id);
    List<Pedido> buscarPorUsuarioId(Long usuarioId);
}