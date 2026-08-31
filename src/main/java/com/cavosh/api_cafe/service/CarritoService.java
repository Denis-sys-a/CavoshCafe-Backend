package com.cavosh.api_cafe.service;

import com.cavosh.api_cafe.dto.*;

public interface CarritoService {
    CarritoResponseDTO obtenerCarritoPorUsuario(Long usuarioId);

    CarritoResponseDTO agregarItem(AgregarCarritoItemRequestDTO dto);

    CarritoResponseDTO actualizarCantidad(Long itemId, ActualizarCarritoItemRequestDTO dto);

    CarritoResponseDTO eliminarItem(Long itemId);
}
