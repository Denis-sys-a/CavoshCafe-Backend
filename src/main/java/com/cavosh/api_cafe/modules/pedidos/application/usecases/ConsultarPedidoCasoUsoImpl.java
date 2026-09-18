package com.cavosh.api_cafe.modules.pedidos.application.usecases;

import com.cavosh.api_cafe.modules.pedidos.domain.model.Pedido;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.in.ConsultarPedidoCasoUso;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.out.PedidoRepositorioPuerto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarPedidoCasoUsoImpl implements ConsultarPedidoCasoUso {

    private final PedidoRepositorioPuerto pedidoRepositorioPuerto;

    @Override
    public Pedido obtenerPorId(Long id) {
        return pedidoRepositorioPuerto.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    @Override
    public List<Pedido> obtenerPorUsuario(Long usuarioId) {
        return pedidoRepositorioPuerto.buscarPorUsuarioId(usuarioId);
    }
}