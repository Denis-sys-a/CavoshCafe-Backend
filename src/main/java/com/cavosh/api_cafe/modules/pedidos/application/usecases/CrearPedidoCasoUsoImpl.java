package com.cavosh.api_cafe.modules.pedidos.application.usecases;

import com.cavosh.api_cafe.modules.pedidos.domain.model.Pedido;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.in.CrearPedidoCasoUso;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.out.PedidoRepositorioPuerto;
import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.in.web.dtos.CrearPedidoRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrearPedidoCasoUsoImpl implements CrearPedidoCasoUso {

    private final PedidoRepositorioPuerto pedidoRepositorioPuerto;

    @Override
    public Pedido crearPedido(CrearPedidoRequestDTO dto) {
        return null; 
    }
}