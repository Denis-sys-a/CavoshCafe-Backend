package com.cavosh.api_cafe.modules.carrito.domain.ports.in;

import com.cavosh.api_cafe.modules.carrito.domain.model.Carrito;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos.AgregarItemDTO;

public interface CarritoCasoUso {
    Carrito obtenerCarritoPorUsuario(Long usuarioId);
    Carrito agregarItem(Long usuarioId, AgregarItemDTO dto);
    Carrito eliminarItem(Long usuarioId, Long itemId);
    void vaciarCarrito(Long usuarioId);
}