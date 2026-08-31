package com.cavosh.api_cafe.service;

import com.cavosh.api_cafe.dto.CheckoutRequestDTO;
import com.cavosh.api_cafe.dto.PedidoResponseDTO;
import java.util.List;

public interface PedidoService {
    PedidoResponseDTO crearPedidoDesdeCarrito(CheckoutRequestDTO dto);

    List<PedidoResponseDTO> obtenerHistorialPorUsuario(Long usuarioId);
}
