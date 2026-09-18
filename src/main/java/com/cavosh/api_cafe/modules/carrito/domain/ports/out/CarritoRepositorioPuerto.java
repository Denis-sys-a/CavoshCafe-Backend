package com.cavosh.api_cafe.modules.carrito.domain.ports.out;

import com.cavosh.api_cafe.modules.carrito.domain.model.Carrito;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos.AgregarItemDTO;

public interface CarritoRepositorioPuerto {
    Carrito obtenerPorUsuarioId(Long usuarioId);
    Carrito guardar(Long usuarioId, AgregarItemDTO dto);
    Carrito eliminarItem(Long usuarioId, Long itemId);
    void vaciar(Long usuarioId);
}